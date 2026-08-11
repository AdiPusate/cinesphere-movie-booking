package com.tramell.cinesphere.controller;

import com.tramell.cinesphere.dto.ApiResponse;
import com.tramell.cinesphere.dto.response.PaymentResponse;
import com.tramell.cinesphere.enums.PaymentStatus;
import com.tramell.cinesphere.service.PaymentService;
import com.tramell.cinesphere.util.ApiResponseUtil;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tramell/cinesphere/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/booking/{bookingId}")
    @PreAuthorize("@bookingService.canAccessBooking(#bookingId, authentication)")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPaymentByBooking(@PathVariable Long bookingId) {
        return ApiResponseUtil.success(paymentService.getPaymentByBooking(bookingId));
    }

    @PutMapping("/{paymentId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PaymentResponse>> updatePaymentStatus(
            @PathVariable Long paymentId,
            @RequestParam PaymentStatus status) {
        
        return ApiResponseUtil.success(
                paymentService.updatePaymentStatus(paymentId, status), 
                "Payment status updated successfully"
        );
    }

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}
