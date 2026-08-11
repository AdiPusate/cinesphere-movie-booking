package com.tramell.cinesphere.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ShowRequest {
    @NotNull(message = "Movie ID is required")
    private Long movieId;

    @NotNull(message = "Theatre ID is required")
    private Long theatreId;

    @NotNull(message = "Show time is required")
    @Future(message = "Show time must be in the future")
    private LocalDateTime showTime;

    @NotNull(message = "Base price is required")
    @Positive(message = "Base price must be positive")
    private BigDecimal basePrice;

    public ShowRequest() {
    }

    public ShowRequest(Long movieId, Long theatreId, LocalDateTime showTime, BigDecimal basePrice) {
        this.movieId = movieId;
        this.theatreId = theatreId;
        this.showTime = showTime;
        this.basePrice = basePrice;
    }

    public Long getMovieId() {
        return movieId;
    }

    public Long getTheatreId() {
        return theatreId;
    }

    public LocalDateTime getShowTime() {
        return showTime;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public void setTheatreId(Long theatreId) {
        this.theatreId = theatreId;
    }

    public void setShowTime(LocalDateTime showTime) {
        this.showTime = showTime;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }
}
