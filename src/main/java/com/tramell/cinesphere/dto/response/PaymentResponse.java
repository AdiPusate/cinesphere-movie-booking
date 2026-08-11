package com.tramell.cinesphere.dto.response;

import com.tramell.cinesphere.enums.PaymentStatus;

import java.math.BigDecimal;

public class PaymentResponse {
    private Long paymentId;
    private BigDecimal amount;
    private PaymentStatus paymentStatus;
    private String transactionId;

    public PaymentResponse() {
    }

    public PaymentResponse(Long paymentId, BigDecimal amount, PaymentStatus paymentStatus, String transactionId) {
        this.paymentId = paymentId;
        this.amount = amount;
        this.paymentStatus = paymentStatus;
        this.transactionId = transactionId;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public static PaymentResponseBuilder builder() {
        return new PaymentResponseBuilder();
    }

    public static class PaymentResponseBuilder {
        private Long paymentId;
        private BigDecimal amount;
        private PaymentStatus paymentStatus;
        private String transactionId;

        public PaymentResponseBuilder paymentId(Long paymentId) {
            this.paymentId = paymentId;
            return this;
        }

        public PaymentResponseBuilder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public PaymentResponseBuilder paymentStatus(PaymentStatus paymentStatus) {
            this.paymentStatus = paymentStatus;
            return this;
        }

        public PaymentResponseBuilder transactionId(String transactionId) {
            this.transactionId = transactionId;
            return this;
        }

        public PaymentResponse build() {
            return new PaymentResponse(this.paymentId, this.amount, this.paymentStatus, this.transactionId);
        }
    }
}
