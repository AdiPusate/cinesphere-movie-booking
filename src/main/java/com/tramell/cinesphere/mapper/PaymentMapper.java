package com.tramell.cinesphere.mapper;

import com.tramell.cinesphere.dto.response.PaymentResponse;
import com.tramell.cinesphere.entity.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {
    public PaymentResponse toResponse(Payment payment) {
        if (payment == null) return null;
        return PaymentResponse.builder()
                .paymentId(payment.getPaymentId())
                .amount(payment.getAmount())
                .paymentStatus(payment.getPaymentStatus())
                .transactionId(payment.getTransactionId())
                .build();
    }
}
