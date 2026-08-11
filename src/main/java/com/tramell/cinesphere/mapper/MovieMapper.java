package com.tramell.cinesphere.mapper;

import com.tramell.cinesphere.dto.response.MovieResponse;
import com.tramell.cinesphere.entity.Movie;
import org.springframework.stereotype.Component;

@Component
public class MovieMapper {
    public MovieResponse toResponse(Movie movie) {
        if (movie == null) return null;
        return MovieResponse.builder()
                .movieId(movie.getMovieId())
                .title(movie.getTitle())
                .genre(movie.getGenre())
                .durationMinutes(movie.getDuration())
                .language(movie.getLanguage())
                .description(movie.getDescription())
                .releaseDate(movie.getReleaseDate())
                .posterUrl(movie.getPosterUrl())
                .bannerUrl(movie.getBannerUrl())
                .rating(movie.getRating())
                .status(movie.getStatus())
                .build();
    }
}
