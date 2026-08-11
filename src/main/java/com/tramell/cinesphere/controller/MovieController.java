package com.tramell.cinesphere.controller;

import com.tramell.cinesphere.dto.ApiResponse;
import com.tramell.cinesphere.dto.request.MovieRequest;
import com.tramell.cinesphere.dto.response.MovieResponse;
import com.tramell.cinesphere.service.MovieService;
import com.tramell.cinesphere.util.ApiResponseUtil;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tramell/cinesphere/movies")
public class MovieController {

    private final MovieService movieService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<MovieResponse>> addMovie(@Valid @RequestBody MovieRequest request) {
        return ApiResponseUtil.created(movieService.addMovie(request), "Movie added successfully");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<MovieResponse>> updateMovie(@PathVariable Long id, @Valid @RequestBody MovieRequest request) {
        return ApiResponseUtil.success(movieService.updateMovie(id, request), "Movie updated successfully");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ApiResponseUtil.deleted("Movie deleted successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MovieResponse>> getMovieById(@PathVariable Long id) {
        return ApiResponseUtil.success(movieService.getMovieById(id));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<MovieResponse>>> getAllMovies() {
        return ApiResponseUtil.success(movieService.getAllMovies());
    }

    @GetMapping("/showing")
    public ResponseEntity<ApiResponse<List<MovieResponse>>> getShowingMovies() {
        return ApiResponseUtil.success(movieService.getShowingMovies());
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<MovieResponse>>> searchMovies(@RequestParam String keyword) {
        return ApiResponseUtil.success(movieService.searchMovies(keyword));
    }

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }
}
