package com.tramell.cinesphere.service;

import com.tramell.cinesphere.dto.request.MovieRequest;
import com.tramell.cinesphere.dto.response.MovieResponse;

import java.util.List;

public interface MovieService {
    MovieResponse addMovie(MovieRequest request);
    MovieResponse updateMovie(Long id, MovieRequest request);
    void deleteMovie(Long id);
    MovieResponse getMovieById(Long id);
    List<MovieResponse> getAllMovies();
    List<MovieResponse> getShowingMovies();
    List<MovieResponse> searchMovies(String keyword);
}
