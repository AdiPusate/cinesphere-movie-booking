package com.tramell.cinesphere.mapper;

import com.tramell.cinesphere.dto.response.TheatreResponse;
import com.tramell.cinesphere.entity.Theatre;
import org.springframework.stereotype.Component;

@Component
public class TheatreMapper {
    public TheatreResponse toResponse(Theatre theatre) {
        if (theatre == null) return null;
        return TheatreResponse.builder()
                .theatreId(theatre.getTheatreId())
                .name(theatre.getName())
                .city(theatre.getCity())
                .address(theatre.getAddress())
                .build();
    }
}
