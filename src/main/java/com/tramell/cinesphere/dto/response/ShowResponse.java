package com.tramell.cinesphere.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ShowResponse {
    private Long showId;
    private MovieResponse movie;
    private TheatreResponse theatre;
    private LocalDateTime showTime;
    private BigDecimal basePrice;

    public ShowResponse() {
    }

    public ShowResponse(Long showId, MovieResponse movie, TheatreResponse theatre, LocalDateTime showTime, BigDecimal basePrice) {
        this.showId = showId;
        this.movie = movie;
        this.theatre = theatre;
        this.showTime = showTime;
        this.basePrice = basePrice;
    }

    public Long getShowId() {
        return showId;
    }

    public MovieResponse getMovie() {
        return movie;
    }

    public TheatreResponse getTheatre() {
        return theatre;
    }

    public LocalDateTime getShowTime() {
        return showTime;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setShowId(Long showId) {
        this.showId = showId;
    }

    public void setMovie(MovieResponse movie) {
        this.movie = movie;
    }

    public void setTheatre(TheatreResponse theatre) {
        this.theatre = theatre;
    }

    public void setShowTime(LocalDateTime showTime) {
        this.showTime = showTime;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public static ShowResponseBuilder builder() {
        return new ShowResponseBuilder();
    }

    public static class ShowResponseBuilder {
        private Long showId;
        private MovieResponse movie;
        private TheatreResponse theatre;
        private LocalDateTime showTime;
        private BigDecimal basePrice;

        public ShowResponseBuilder showId(Long showId) {
            this.showId = showId;
            return this;
        }

        public ShowResponseBuilder movie(MovieResponse movie) {
            this.movie = movie;
            return this;
        }

        public ShowResponseBuilder theatre(TheatreResponse theatre) {
            this.theatre = theatre;
            return this;
        }

        public ShowResponseBuilder showTime(LocalDateTime showTime) {
            this.showTime = showTime;
            return this;
        }

        public ShowResponseBuilder basePrice(BigDecimal basePrice) {
            this.basePrice = basePrice;
            return this;
        }

        public ShowResponse build() {
            return new ShowResponse(this.showId, this.movie, this.theatre, this.showTime, this.basePrice);
        }
    }
}
