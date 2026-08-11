package com.tramell.cinesphere.controller;

import com.tramell.cinesphere.dto.ApiResponse;
import com.tramell.cinesphere.dto.request.BookingRequest;
import com.tramell.cinesphere.dto.response.BookingResponse;
import com.tramell.cinesphere.service.BookingService;
import com.tramell.cinesphere.util.ApiResponseUtil;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tramell/cinesphere/bookings")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == principal.userId")
    public ResponseEntity<ApiResponse<BookingResponse>> createBooking(
            @PathVariable Long userId,
            @Valid @RequestBody BookingRequest request) {
        
        return ApiResponseUtil.created(
                bookingService.createBooking(userId, request), 
                "Booking created successfully"
        );
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == principal.userId")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getUserBookings(@PathVariable Long userId) {
        return ApiResponseUtil.success(bookingService.getUserBookings(userId));
    }

    @GetMapping("/{bookingId}")
    @PreAuthorize("hasRole('ADMIN') or @bookingService.canAccessBooking(#bookingId, authentication)")
    public ResponseEntity<ApiResponse<BookingResponse>> getBookingById(@PathVariable Long bookingId) {
        return ApiResponseUtil.success(bookingService.getBookingById(bookingId));
    }

    @PostMapping("/{bookingId}/cancel")
    @PreAuthorize("hasRole('ADMIN') or @bookingService.canAccessBooking(#bookingId, authentication)")
    public ResponseEntity<ApiResponse<BookingResponse>> cancelBooking(@PathVariable Long bookingId) {
        return ApiResponseUtil.success(bookingService.cancelBooking(bookingId), "Booking cancelled successfully");
    }

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }
}
