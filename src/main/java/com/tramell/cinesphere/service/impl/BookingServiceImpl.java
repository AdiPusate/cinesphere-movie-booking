package com.tramell.cinesphere.service.impl;

import com.tramell.cinesphere.dto.request.BookingRequest;
import com.tramell.cinesphere.dto.response.BookingResponse;
import com.tramell.cinesphere.entity.Booking;
import com.tramell.cinesphere.entity.Coupon;
import com.tramell.cinesphere.entity.Show;
import com.tramell.cinesphere.entity.User;
import com.tramell.cinesphere.enums.BookingStatus;
import com.tramell.cinesphere.exception.BadRequestException;
import com.tramell.cinesphere.exception.ResourceNotFoundException;
import com.tramell.cinesphere.mapper.BookingMapper;
import com.tramell.cinesphere.repository.BookingRepository;
import com.tramell.cinesphere.repository.CouponRepository;
import com.tramell.cinesphere.repository.ShowRepository;
import com.tramell.cinesphere.repository.UserRepository;
import com.tramell.cinesphere.service.BookingService;
import com.tramell.cinesphere.service.CouponService;
import com.tramell.cinesphere.service.PaymentService;
import com.tramell.cinesphere.service.ShowSeatService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

@Service("bookingService")
public class BookingServiceImpl implements BookingService {

    private static final int MAX_SEATS_PER_BOOKING = 10;

    @Value("${application.base-url:http://localhost:8080}")
    private String baseUrl;

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ShowRepository showRepository;
    private final CouponRepository couponRepository;
    private final ShowSeatService showSeatService;
    private final CouponService couponService;
    private final PaymentService paymentService;
    private final BookingMapper bookingMapper;

    @Override
    @Transactional
    public BookingResponse createBooking(Long userId, BookingRequest request) {
        validateSeatRequest(request.getSeatNumbers());

        User user = findUser(userId);
        Show show = findShow(request.getShowId());

        if (!show.getShowTime().isAfter(LocalDateTime.now())) {
            throw new BadRequestException("This show has already started or ended");
        }

        List<String> seatNumbers = request.getSeatNumbers().stream()
                .map(String::trim)
                .map(value -> value.toUpperCase(Locale.ROOT))
                .distinct()
                .toList();

        if (seatNumbers.size() != request.getSeatNumbers().size()) {
            throw new BadRequestException("Duplicate seat numbers are not allowed");
        }

        // Pessimistic row locks prevent two concurrent requests from booking
        // the same show seat.
        showSeatService.lockSeats(show.getShowId(), seatNumbers);

        Coupon coupon = applyCoupon(request.getCouponCode());

        BigDecimal subtotal = calculateTotal(show.getBasePrice(), seatNumbers.size());
        BigDecimal totalAmount = coupon == null
                ? subtotal
                : calculateDiscountedTotal(subtotal, coupon);

        Booking booking = Booking.builder()
                .user(user)
                .show(show)
                .coupon(coupon)
                .bookingDate(LocalDateTime.now())
                .totalAmount(totalAmount)
                .bookingStatus(BookingStatus.CONFIRMED)
                .build();

        booking = bookingRepository.save(booking);

        paymentService.createPayment(booking, totalAmount);

        // Associate every booked seat with this booking. This is important for
        // booking history and receipt generation.
        showSeatService.bookSeats(show.getShowId(), seatNumbers, booking);

        BookingResponse response = bookingMapper.toResponse(booking, seatNumbers);
        response.setReceiptId("rcpt_" + booking.getBookingId());
        response.setReceiptUrl(receiptUrl(booking.getBookingId()));

        return response;
    }

    @Override
    @Transactional
    public BookingResponse cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", bookingId));

        if (booking.getBookingStatus() == BookingStatus.CANCELLED) {
            throw new BadRequestException("Booking is already cancelled");
        }
        
        booking.setBookingStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);

        if (booking.getPayment() != null && booking.getPayment().getPaymentStatus() != com.tramell.cinesphere.enums.PaymentStatus.REFUNDED) {
            paymentService.updatePaymentStatus(booking.getPayment().getPaymentId(), com.tramell.cinesphere.enums.PaymentStatus.REFUNDED);
        }

        List<String> seatNumbers = booking.getBookedSeats().stream()
                .map(seat -> seat.getSeatNumber())
                .collect(Collectors.toList());
        
        if (!seatNumbers.isEmpty()) {
            showSeatService.releaseSeats(booking.getShow().getShowId(), seatNumbers);
        }

        BookingResponse response = bookingMapper.toResponse(booking, seatNumbers);
        response.setReceiptId("rcpt_" + booking.getBookingId());
        response.setReceiptUrl(receiptUrl(booking.getBookingId()));
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public BookingResponse getBookingById(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", bookingId));

        List<String> seatNumbers = booking.getBookedSeats().stream()
                .map(seat -> seat.getSeatNumber())
                .collect(Collectors.toList());

        BookingResponse response = bookingMapper.toResponse(booking, seatNumbers);
        response.setReceiptId("rcpt_" + booking.getBookingId());
        response.setReceiptUrl(receiptUrl(booking.getBookingId()));
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> getUserBookings(Long userId) {
        return bookingRepository.findByUserUserId(userId).stream()
                .map(booking -> {
                    List<String> bookedSeats = booking.getBookedSeats().stream()
                            .map(seat -> seat.getSeatNumber())
                            .collect(Collectors.toList());

                    BookingResponse response =
                            bookingMapper.toResponse(booking, bookedSeats);
                    response.setReceiptId("rcpt_" + booking.getBookingId());
                    response.setReceiptUrl(receiptUrl(booking.getBookingId()));
                    return response;
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canAccessBooking(Long bookingId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        if (authentication.getAuthorities().contains(
                new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return true;
        }

        if (!(authentication.getPrincipal() instanceof User user)) {
            return false;
        }

        return bookingRepository.findById(bookingId)
                .map(booking -> booking.getUser().getUserId().equals(user.getUserId()))
                .orElse(false);
    }

    private void validateSeatRequest(List<String> seatNumbers) {
        if (seatNumbers == null || seatNumbers.isEmpty()) {
            throw new BadRequestException("At least one seat must be selected");
        }
        if (seatNumbers.size() > MAX_SEATS_PER_BOOKING) {
            throw new BadRequestException(
                    "Maximum " + MAX_SEATS_PER_BOOKING + " seats allowed per booking");
        }
        if (seatNumbers.stream().anyMatch(Objects::isNull)) {
            throw new BadRequestException("Seat number cannot be null");
        }
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
    }

    private Show findShow(Long showId) {
        return showRepository.findById(showId)
                .orElseThrow(() -> new ResourceNotFoundException("Show", "id", showId));
    }

    private BigDecimal calculateTotal(BigDecimal basePrice, int seatCount) {
        return basePrice.multiply(BigDecimal.valueOf(seatCount))
                .setScale(2, RoundingMode.HALF_UP);
    }

    private Coupon applyCoupon(String couponCode) {
        if (couponCode == null || couponCode.isBlank()) {
            return null;
        }

        var validCoupon = couponService.validateCoupon(couponCode.trim());
        return couponRepository.findById(validCoupon.getCouponId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Coupon", "id", validCoupon.getCouponId()));
    }

    private BigDecimal calculateDiscountedTotal(
            BigDecimal totalAmount, Coupon coupon) {
        BigDecimal discount = totalAmount
                .multiply(BigDecimal.valueOf(coupon.getDiscountPercentage()))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        return totalAmount.subtract(discount).setScale(2, RoundingMode.HALF_UP);
    }

    private String receiptUrl(Long bookingId) {
        return baseUrl + "/tramell/cinesphere/receipt/" + bookingId;
    }

    public BookingServiceImpl(
            BookingRepository bookingRepository,
            UserRepository userRepository,
            ShowRepository showRepository,
            CouponRepository couponRepository,
            ShowSeatService showSeatService,
            CouponService couponService,
            PaymentService paymentService,
            BookingMapper bookingMapper) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.showRepository = showRepository;
        this.couponRepository = couponRepository;
        this.showSeatService = showSeatService;
        this.couponService = couponService;
        this.paymentService = paymentService;
        this.bookingMapper = bookingMapper;
    }
}
