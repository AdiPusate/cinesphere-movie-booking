package com.tramell.cinesphere.service;

import com.tramell.cinesphere.dto.request.TheatreRequest;
import com.tramell.cinesphere.dto.response.TheatreResponse;

import java.util.List;

public interface TheatreService {
    TheatreResponse addTheatre(TheatreRequest request);
    List<TheatreResponse> getAllTheatres();
    TheatreResponse getTheatreById(Long id);
    TheatreResponse updateTheatre(Long id, TheatreRequest request);
    void deleteTheatre(Long id);
}
