package com.tramell.cinesphere.service;

import com.tramell.cinesphere.dto.request.ShowRequest;
import com.tramell.cinesphere.dto.response.ShowResponse;

import java.util.List;

public interface ShowService {
    ShowResponse addShow(ShowRequest request);
    ShowResponse getShowById(Long id);
    List<ShowResponse> getShowsByMovie(Long movieId);
    List<ShowResponse> getShowsByTheatre(Long theatreId);
}
