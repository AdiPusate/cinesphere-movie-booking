package com.tramell.cinesphere.repository;

import com.tramell.cinesphere.entity.Booking;
import com.tramell.cinesphere.entity.Show;
import com.tramell.cinesphere.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserUserId(Long userId);
    List<Booking> findByBookingStatus(BookingStatus status);
    long countByBookingStatus(BookingStatus status);
    List<Booking> findByShow(Show show);
    List<Booking> findByBookingDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT SUM(b.totalAmount) FROM Booking b WHERE b.bookingStatus = :status")
    BigDecimal getTotalRevenueByStatus(@org.springframework.data.repository.query.Param("status") BookingStatus status);
}
