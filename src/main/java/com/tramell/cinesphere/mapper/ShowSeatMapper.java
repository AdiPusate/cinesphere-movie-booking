package com.tramell.cinesphere.mapper;

import com.tramell.cinesphere.dto.response.ShowSeatResponse;
import com.tramell.cinesphere.entity.ShowSeat;
import org.springframework.stereotype.Component;

@Component
public class ShowSeatMapper {
    public ShowSeatResponse toResponse(ShowSeat seat) {
        if (seat == null) return null;
        return ShowSeatResponse.builder()
                .seatId(seat.getSeatId())
                .seatNumber(seat.getSeatNumber())
                .status(seat.getStatus())
                .build();
    }
}
