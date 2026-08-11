package com.tramell.cinesphere.mapper;

import com.tramell.cinesphere.dto.response.BookingResponse;
import com.tramell.cinesphere.entity.Booking;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookingMapper {

    private final ShowMapper showMapper;

    public BookingResponse toResponse(Booking booking) {
        if (booking == null) return null;
        
        List<String> bookedSeats = null;
        if (booking.getBookedSeats() != null) {
            bookedSeats = booking.getBookedSeats().stream()
                    .map(seat -> seat.getSeatNumber())
                    .collect(Collectors.toList());
        }

        return BookingResponse.builder()
                .bookingId(booking.getBookingId())
                .show(showMapper.toResponse(booking.getShow()))
                .totalAmount(booking.getTotalAmount())
                .bookingStatus(booking.getBookingStatus())
                .bookedSeats(bookedSeats)
                .bookingDate(booking.getBookingDate())
                .build();
    }
    
    public BookingResponse toResponse(Booking booking, List<String> overrideBookedSeats) {
        if (booking == null) return null;
        return BookingResponse.builder()
                .bookingId(booking.getBookingId())
                .show(showMapper.toResponse(booking.getShow()))
                .totalAmount(booking.getTotalAmount())
                .bookingStatus(booking.getBookingStatus())
                .bookedSeats(overrideBookedSeats)
                .bookingDate(booking.getBookingDate())
                .build();
    }

    public BookingMapper(ShowMapper showMapper) {
        this.showMapper = showMapper;
    }
}
