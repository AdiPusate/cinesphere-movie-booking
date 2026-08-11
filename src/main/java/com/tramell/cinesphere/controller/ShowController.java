package com.tramell.cinesphere.controller;

import com.tramell.cinesphere.dto.ApiResponse;
import com.tramell.cinesphere.dto.request.ShowRequest;
import com.tramell.cinesphere.dto.response.ShowResponse;
import com.tramell.cinesphere.dto.response.ShowSeatResponse;
import com.tramell.cinesphere.service.ShowSeatService;
import com.tramell.cinesphere.service.ShowService;
import com.tramell.cinesphere.util.ApiResponseUtil;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tramell/cinesphere/shows")
public class ShowController {

    private final ShowService showService;
    private final ShowSeatService showSeatService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ShowResponse>> addShow(@Valid @RequestBody ShowRequest request) {
        return ApiResponseUtil.created(showService.addShow(request), "Show added successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ShowResponse>> getShowById(@PathVariable Long id) {
        return ApiResponseUtil.success(showService.getShowById(id));
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<ApiResponse<List<ShowResponse>>> getShowsByMovie(@PathVariable Long movieId) {
        return ApiResponseUtil.success(showService.getShowsByMovie(movieId));
    }

    @GetMapping("/theatre/{theatreId}")
    public ResponseEntity<ApiResponse<List<ShowResponse>>> getShowsByTheatre(@PathVariable Long theatreId) {
        return ApiResponseUtil.success(showService.getShowsByTheatre(theatreId));
    }
    
    @GetMapping("/{showId}/seats")
    public ResponseEntity<ApiResponse<List<ShowSeatResponse>>> getShowSeats(@PathVariable Long showId) {
        return ApiResponseUtil.success(showSeatService.getSeatsForShow(showId));
    }

    @PostMapping("/{showId}/seats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> generateSeats(
            @PathVariable Long showId,
            @RequestParam(defaultValue = "5") int rows,
            @RequestParam(defaultValue = "10") int seatsPerRow) {
        
        showSeatService.generateSeatsForShow(showId, rows, seatsPerRow);
        return ApiResponseUtil.success(null, "Seats generated successfully");
    }

    public ShowController(ShowService showService, ShowSeatService showSeatService) {
        this.showService = showService;
        this.showSeatService = showSeatService;
    }
}
