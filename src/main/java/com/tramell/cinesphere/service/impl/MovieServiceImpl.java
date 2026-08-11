package com.tramell.cinesphere.service.impl;

import com.tramell.cinesphere.dto.request.MovieRequest;
import com.tramell.cinesphere.dto.response.MovieResponse;
import com.tramell.cinesphere.entity.Movie;
import com.tramell.cinesphere.enums.MovieStatus;
import com.tramell.cinesphere.exception.ResourceNotFoundException;
import com.tramell.cinesphere.mapper.MovieMapper;
import com.tramell.cinesphere.repository.MovieRepository;
import com.tramell.cinesphere.repository.ShowRepository;
import com.tramell.cinesphere.service.MovieService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;
    private final ShowRepository showRepository;

    @Override
    @Transactional
    public MovieResponse addMovie(MovieRequest request) {
        Movie movie = Movie.builder()
                .title(request.getTitle())
                .genre(request.getGenre())
                .duration(request.getDurationMinutes())
                .language(request.getLanguage())
                .description(request.getDescription())
                .releaseDate(request.getReleaseDate())
                .posterUrl(request.getPosterUrl())
                .bannerUrl(request.getBannerUrl())
                .rating(request.getRating())
                .status(request.getStatus())
                .build();
        Movie savedMovie = movieRepository.save(movie);
        return movieMapper.toResponse(savedMovie);
    }

    @Override
    @Transactional
    public MovieResponse updateMovie(Long id, MovieRequest request) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", id));

        movie.setTitle(request.getTitle());
        movie.setGenre(request.getGenre());
        movie.setDuration(request.getDurationMinutes());
        movie.setLanguage(request.getLanguage());
        movie.setDescription(request.getDescription());
        movie.setReleaseDate(request.getReleaseDate());
        movie.setPosterUrl(request.getPosterUrl());
        movie.setBannerUrl(request.getBannerUrl());
        movie.setRating(request.getRating());
        movie.setStatus(request.getStatus());

        Movie updatedMovie = movieRepository.save(movie);
        return movieMapper.toResponse(updatedMovie);
    }

    @Override
    @Transactional
    public void deleteMovie(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", id));

        movieRepository.delete(movie);
    }

    @Override
    public MovieResponse getMovieById(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", id));
        return movieMapper.toResponse(movie);
    }

    @Override
    public List<MovieResponse> getAllMovies() {
        return movieRepository.findAll().stream()
                .map(movieMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MovieResponse> getShowingMovies() {
        return movieRepository.findByStatus(MovieStatus.NOW_SHOWING).stream()
                .map(movieMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MovieResponse> searchMovies(String keyword) {
        return movieRepository.findByTitleContainingIgnoreCase(keyword).stream()
                .map(movieMapper::toResponse)
                .collect(Collectors.toList());
    }

    public MovieServiceImpl(MovieRepository movieRepository, MovieMapper movieMapper, ShowRepository showRepository) {
        this.movieRepository = movieRepository;
        this.movieMapper = movieMapper;
        this.showRepository = showRepository;
    }
}
