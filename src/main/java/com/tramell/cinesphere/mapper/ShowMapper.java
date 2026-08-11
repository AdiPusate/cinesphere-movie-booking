package com.tramell.cinesphere.mapper;

import com.tramell.cinesphere.dto.response.ShowResponse;
import com.tramell.cinesphere.entity.Show;

import org.springframework.stereotype.Component;

@Component
public class ShowMapper {

    private final MovieMapper movieMapper;
    private final TheatreMapper theatreMapper;

    public ShowResponse toResponse(Show show) {
        if (show == null) return null;
        return ShowResponse.builder()
                .showId(show.getShowId())
                .movie(movieMapper.toResponse(show.getMovie()))
                .theatre(theatreMapper.toResponse(show.getTheatre()))
                .showTime(show.getShowTime())
                .basePrice(show.getBasePrice())
                .build();
    }

    public ShowMapper(MovieMapper movieMapper, TheatreMapper theatreMapper) {
        this.movieMapper = movieMapper;
        this.theatreMapper = theatreMapper;
    }
}
