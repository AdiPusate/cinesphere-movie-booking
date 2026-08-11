package com.tramell.cinesphere.service;

import com.tramell.cinesphere.dto.response.PaymentResponse;
import com.tramell.cinesphere.entity.Booking;
import com.tramell.cinesphere.enums.PaymentStatus;

import java.math.BigDecimal;

public interface PaymentService {
    PaymentResponse createPayment(Booking booking, BigDecimal amount);
    PaymentResponse getPaymentByBooking(Long bookingId);
    PaymentResponse updatePaymentStatus(Long paymentId, PaymentStatus status);
}
