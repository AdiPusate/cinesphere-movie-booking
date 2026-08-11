package com.tramell.cinesphere.service.impl;

import com.tramell.cinesphere.dto.response.PaymentResponse;
import com.tramell.cinesphere.entity.Booking;
import com.tramell.cinesphere.entity.Payment;
import com.tramell.cinesphere.enums.PaymentStatus;
import com.tramell.cinesphere.exception.ResourceNotFoundException;
import com.tramell.cinesphere.mapper.PaymentMapper;
import com.tramell.cinesphere.repository.PaymentRepository;
import com.tramell.cinesphere.service.PaymentService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final com.tramell.cinesphere.service.BookingService bookingService;

    @Override
    @Transactional
    public PaymentResponse createPayment(Booking booking, BigDecimal amount) {
        Payment payment = Payment.builder()
                .booking(booking)
                .amount(amount)
                .paymentStatus(PaymentStatus.SUCCESS)
                .transactionId("TXN-" + UUID.randomUUID().toString())
                .build();
                
        Payment savedPayment = paymentRepository.save(payment);
        booking.setPayment(savedPayment);
        return paymentMapper.toResponse(savedPayment);
    }

    @Override
    public PaymentResponse getPaymentByBooking(Long bookingId) {
        Payment payment = paymentRepository.findByBookingBookingId(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "bookingId", bookingId));
        return paymentMapper.toResponse(payment);
    }

    @Override
    @Transactional
    public PaymentResponse updatePaymentStatus(Long paymentId, PaymentStatus status) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "id", paymentId));
        payment.setPaymentStatus(status);
        payment = paymentRepository.save(payment);
        
        if (status == PaymentStatus.REFUNDED && payment.getBooking().getBookingStatus() != com.tramell.cinesphere.enums.BookingStatus.CANCELLED) {
            bookingService.cancelBooking(payment.getBooking().getBookingId());
        }
        
        return paymentMapper.toResponse(payment);
    }

    public PaymentServiceImpl(PaymentRepository paymentRepository, PaymentMapper paymentMapper, @org.springframework.context.annotation.Lazy com.tramell.cinesphere.service.BookingService bookingService) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
        this.bookingService = bookingService;
    }
}
