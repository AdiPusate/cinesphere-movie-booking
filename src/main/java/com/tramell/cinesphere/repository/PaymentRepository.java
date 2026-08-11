package com.tramell.cinesphere.repository;

import com.tramell.cinesphere.entity.Payment;
import com.tramell.cinesphere.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByBookingBookingId(Long bookingId);
    List<Payment> findByPaymentStatus(PaymentStatus paymentStatus);
}
