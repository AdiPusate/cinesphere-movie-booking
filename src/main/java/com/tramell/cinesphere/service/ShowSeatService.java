package com.tramell.cinesphere.service;

import com.tramell.cinesphere.dto.response.ShowSeatResponse;
import com.tramell.cinesphere.entity.Booking;

import java.util.List;

public interface ShowSeatService {
    List<ShowSeatResponse> getSeatsForShow(Long showId);
    void generateSeatsForShow(Long showId, int rows, int seatsPerRow);
    void lockSeats(Long showId, List<String> seatNumbers);
    void bookSeats(Long showId, List<String> seatNumbers, Booking booking);
    void releaseSeats(Long showId, List<String> seatNumbers);
}
