package com.tramell.cinesphere.service;

import com.tramell.cinesphere.dto.request.BookingRequest;
import com.tramell.cinesphere.dto.response.BookingResponse;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface BookingService {
    BookingResponse createBooking(Long userId, BookingRequest request);
    List<BookingResponse> getUserBookings(Long userId);
    boolean canAccessBooking(Long bookingId, Authentication authentication);
    BookingResponse getBookingById(Long bookingId);
    BookingResponse cancelBooking(Long bookingId);
}
