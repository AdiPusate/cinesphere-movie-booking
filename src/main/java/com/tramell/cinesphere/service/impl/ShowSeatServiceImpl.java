package com.tramell.cinesphere.service.impl;

import com.tramell.cinesphere.dto.response.ShowSeatResponse;
import com.tramell.cinesphere.entity.Show;
import com.tramell.cinesphere.entity.Booking;
import com.tramell.cinesphere.entity.ShowSeat;
import com.tramell.cinesphere.enums.SeatStatus;
import com.tramell.cinesphere.exception.SeatAlreadyBookedException;

import com.tramell.cinesphere.exception.ResourceNotFoundException;
import com.tramell.cinesphere.mapper.ShowSeatMapper;
import com.tramell.cinesphere.repository.ShowRepository;
import com.tramell.cinesphere.repository.ShowSeatRepository;
import com.tramell.cinesphere.service.ShowSeatService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShowSeatServiceImpl implements ShowSeatService {

    private final ShowSeatRepository showSeatRepository;
    private final ShowRepository showRepository;
    private final ShowSeatMapper showSeatMapper;

    @Override
    public List<ShowSeatResponse> getSeatsForShow(Long showId) {
        Show show = getShow(showId);
        return showSeatRepository.findByShow(show).stream()
                .map(showSeatMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void generateSeatsForShow(Long showId, int rows, int seatsPerRow) {
        if (rows < 1 || rows > 26 || seatsPerRow < 1 || seatsPerRow > 50) {
            throw new IllegalArgumentException("Rows must be 1-26 and seats per row must be 1-50");
        }

        Show show = getShow(showId);
        for (int row = 0; row < rows; row++) {
            char rowChar = (char) ('A' + row);
            for (int seatNum = 1; seatNum <= seatsPerRow; seatNum++) {
                String seatNumber = rowChar + String.valueOf(seatNum);
                if (showSeatRepository.findByShowAndSeatNumber(show, seatNumber).isEmpty()) {
                    ShowSeat seat = ShowSeat.builder()
                            .show(show)
                            .seatNumber(seatNumber)
                            .status(SeatStatus.AVAILABLE)
                            .build();
                    showSeatRepository.save(seat);
                }
            }
        }
    }

    @Override
    public void lockSeats(Long showId, List<String> seatNumbers) {
        updateSeatStatus(showId, seatNumbers, SeatStatus.AVAILABLE, SeatStatus.LOCKED);
    }

    @Override
    public void bookSeats(Long showId, List<String> seatNumbers, Booking booking) {
        Show show = getShow(showId);
        for (String seatNumber : seatNumbers) {
            ShowSeat seat = showSeatRepository.findByShowAndSeatNumberForUpdate(show, seatNumber)
                    .orElseThrow(() -> new ResourceNotFoundException("ShowSeat", "seatNumber", seatNumber));

            if (seat.getStatus() != SeatStatus.LOCKED) {
                throw new SeatAlreadyBookedException(
                        "Seat " + seatNumber + " is no longer available.");
            }

            seat.setStatus(SeatStatus.BOOKED);
            seat.setBooking(booking);
            showSeatRepository.save(seat);
        }
    }

    @Override
    public void releaseSeats(Long showId, List<String> seatNumbers) {
        Show show = getShow(showId);
        for (String seatNumber : seatNumbers) {
            ShowSeat seat = showSeatRepository.findByShowAndSeatNumberForUpdate(show, seatNumber)
                    .orElseThrow(() -> new ResourceNotFoundException("ShowSeat", "seatNumber", seatNumber));
            if (seat.getStatus() == SeatStatus.LOCKED) {
                seat.setStatus(SeatStatus.AVAILABLE);
                seat.setBooking(null);
                showSeatRepository.save(seat);
            }
        }
    }
    
    private void updateSeatStatus(Long showId, List<String> seatNumbers, SeatStatus expectedCurrent, SeatStatus next) {
        Show show = getShow(showId);
        for (String seatNumber : seatNumbers) {
            ShowSeat seat = showSeatRepository.findByShowAndSeatNumberForUpdate(show, seatNumber)
                    .orElseThrow(() -> new ResourceNotFoundException("ShowSeat", "seatNumber", seatNumber));
            
            if (seat.getStatus() != expectedCurrent) {
                 throw new SeatAlreadyBookedException("Seat " + seatNumber + " is not available or in incorrect state.");
            }
            
            seat.setStatus(next);
            showSeatRepository.save(seat);
        }
    }
    
    private Show getShow(Long showId) {
        return showRepository.findById(showId)
                .orElseThrow(() -> new ResourceNotFoundException("Show", "id", showId));
    }

    public ShowSeatServiceImpl(ShowSeatRepository showSeatRepository, ShowRepository showRepository, ShowSeatMapper showSeatMapper) {
        this.showSeatRepository = showSeatRepository;
        this.showRepository = showRepository;
        this.showSeatMapper = showSeatMapper;
    }
}
