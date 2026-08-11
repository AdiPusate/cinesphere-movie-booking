package com.tramell.cinesphere.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Long bookingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "show_id", nullable = false)
    private Show show;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @Column(name = "booking_date", nullable = false)
    private LocalDateTime bookingDate;

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "booking_status", nullable = false)
    private com.tramell.cinesphere.enums.BookingStatus bookingStatus; // e.g., CONFIRMED, CANCELLED

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
private List<ShowSeat> bookedSeats = new ArrayList<>();

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL)
    private Payment payment;

    public Booking() {
    }

    public Booking(Long bookingId, User user, Show show, Coupon coupon, LocalDateTime bookingDate, BigDecimal totalAmount, com.tramell.cinesphere.enums.BookingStatus bookingStatus, List<ShowSeat> bookedSeats, Payment payment) {
        this.bookingId = bookingId;
        this.user = user;
        this.show = show;
        this.coupon = coupon;
        this.bookingDate = bookingDate;
        this.totalAmount = totalAmount;
        this.bookingStatus = bookingStatus;
        this.bookedSeats = bookedSeats;
        this.payment = payment;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public User getUser() {
        return user;
    }

    public Show getShow() {
        return show;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public com.tramell.cinesphere.enums.BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    public List<ShowSeat> getBookedSeats() {
        return bookedSeats;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setShow(Show show) {
        this.show = show;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setBookingStatus(com.tramell.cinesphere.enums.BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public void setBookedSeats(List<ShowSeat> bookedSeats) {
        this.bookedSeats = bookedSeats;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public static BookingBuilder builder() {
        return new BookingBuilder();
    }

    public static class BookingBuilder {
        private Long bookingId;
        private User user;
        private Show show;
        private Coupon coupon;
        private LocalDateTime bookingDate;
        private BigDecimal totalAmount;
        private com.tramell.cinesphere.enums.BookingStatus bookingStatus;
        private List<ShowSeat> bookedSeats;
        private Payment payment;

        public BookingBuilder bookingId(Long bookingId) {
            this.bookingId = bookingId;
            return this;
        }

        public BookingBuilder user(User user) {
            this.user = user;
            return this;
        }

        public BookingBuilder show(Show show) {
            this.show = show;
            return this;
        }

        public BookingBuilder coupon(Coupon coupon) {
            this.coupon = coupon;
            return this;
        }

        public BookingBuilder bookingDate(LocalDateTime bookingDate) {
            this.bookingDate = bookingDate;
            return this;
        }

        public BookingBuilder totalAmount(BigDecimal totalAmount) {
            this.totalAmount = totalAmount;
            return this;
        }

        public BookingBuilder bookingStatus(com.tramell.cinesphere.enums.BookingStatus bookingStatus) {
            this.bookingStatus = bookingStatus;
            return this;
        }

        public BookingBuilder bookedSeats(List<ShowSeat> bookedSeats) {
            this.bookedSeats = bookedSeats;
            return this;
        }

        public BookingBuilder payment(Payment payment) {
            this.payment = payment;
            return this;
        }

        public Booking build() {
            return new Booking(this.bookingId, this.user, this.show, this.coupon, this.bookingDate, this.totalAmount, this.bookingStatus, this.bookedSeats, this.payment);
        }
    }
}
