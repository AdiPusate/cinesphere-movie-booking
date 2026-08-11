package com.tramell.cinesphere.repository;

import com.tramell.cinesphere.entity.Show;
import com.tramell.cinesphere.entity.ShowSeat;
import com.tramell.cinesphere.enums.SeatStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShowSeatRepository extends JpaRepository<ShowSeat, Long> {
    List<ShowSeat> findByBookingBookingId(Long bookingId);
    List<ShowSeat> findByShow(Show show);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from ShowSeat s where s.show = :show and s.seatNumber = :seatNumber")
    Optional<ShowSeat> findByShowAndSeatNumberForUpdate(
            @Param("show") Show show,
            @Param("seatNumber") String seatNumber);

    Optional<ShowSeat> findByShowAndSeatNumber(Show show, String seatNumber);
    List<ShowSeat> findByShowAndStatus(Show show, SeatStatus status);
}
