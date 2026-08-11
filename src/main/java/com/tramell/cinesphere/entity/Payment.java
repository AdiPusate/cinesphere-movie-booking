package com.tramell.cinesphere.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false, unique = true)
    private Booking booking;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private com.tramell.cinesphere.enums.PaymentStatus paymentStatus; // e.g., PENDING, SUCCESS

    @Column(name = "transaction_id")
    private String transactionId;

    public Payment() {
    }

    public Payment(Long paymentId, Booking booking, BigDecimal amount, com.tramell.cinesphere.enums.PaymentStatus paymentStatus, String transactionId) {
        this.paymentId = paymentId;
        this.booking = booking;
        this.amount = amount;
        this.paymentStatus = paymentStatus;
        this.transactionId = transactionId;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public Booking getBooking() {
        return booking;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public com.tramell.cinesphere.enums.PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setPaymentStatus(com.tramell.cinesphere.enums.PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public static PaymentBuilder builder() {
        return new PaymentBuilder();
    }

    public static class PaymentBuilder {
        private Long paymentId;
        private Booking booking;
        private BigDecimal amount;
        private com.tramell.cinesphere.enums.PaymentStatus paymentStatus;
        private String transactionId;

        public PaymentBuilder paymentId(Long paymentId) {
            this.paymentId = paymentId;
            return this;
        }

        public PaymentBuilder booking(Booking booking) {
            this.booking = booking;
            return this;
        }

        public PaymentBuilder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public PaymentBuilder paymentStatus(com.tramell.cinesphere.enums.PaymentStatus paymentStatus) {
            this.paymentStatus = paymentStatus;
            return this;
        }

        public PaymentBuilder transactionId(String transactionId) {
            this.transactionId = transactionId;
            return this;
        }

        public Payment build() {
            return new Payment(this.paymentId, this.booking, this.amount, this.paymentStatus, this.transactionId);
        }
    }
}
