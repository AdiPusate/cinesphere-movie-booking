package com.tramell.cinesphere.controller;

import com.tramell.cinesphere.dto.ApiResponse;
import com.tramell.cinesphere.dto.request.TheatreRequest;
import com.tramell.cinesphere.dto.response.TheatreResponse;
import com.tramell.cinesphere.service.TheatreService;
import com.tramell.cinesphere.util.ApiResponseUtil;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tramell/cinesphere/theatres")
public class TheatreController {

    private final TheatreService theatreService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TheatreResponse>> addTheatre(@Valid @RequestBody TheatreRequest request) {
        return ApiResponseUtil.created(theatreService.addTheatre(request), "Theatre added successfully");
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TheatreResponse>>> getAllTheatres() {
        return ApiResponseUtil.success(theatreService.getAllTheatres());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TheatreResponse>> getTheatreById(@PathVariable Long id) {
        return ApiResponseUtil.success(theatreService.getTheatreById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TheatreResponse>> updateTheatre(@PathVariable Long id, @Valid @RequestBody TheatreRequest request) {
        return ApiResponseUtil.success(theatreService.updateTheatre(id, request), "Theatre updated successfully");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteTheatre(@PathVariable Long id) {
        theatreService.deleteTheatre(id);
        return ApiResponseUtil.success(null, "Theatre deleted successfully");
    }

    public TheatreController(TheatreService theatreService) {
        this.theatreService = theatreService;
    }
}
