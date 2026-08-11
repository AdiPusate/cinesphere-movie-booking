package com.tramell.cinesphere.dto.response;

import com.tramell.cinesphere.enums.MovieStatus;

import java.time.LocalDate;

public class MovieResponse {
    private Long movieId;
    private String title;
    private String genre;
    private Integer durationMinutes;
    private String language;
    private String description;
    private LocalDate releaseDate;
    private String posterUrl;
    private String bannerUrl;
    private Double rating;
    private MovieStatus status;

    public MovieResponse() {
    }

    public MovieResponse(Long movieId, String title, String genre, Integer durationMinutes, String language, String description, LocalDate releaseDate, String posterUrl, String bannerUrl, Double rating, MovieStatus status) {
        this.movieId = movieId;
        this.title = title;
        this.genre = genre;
        this.durationMinutes = durationMinutes;
        this.language = language;
        this.description = description;
        this.releaseDate = releaseDate;
        this.posterUrl = posterUrl;
        this.bannerUrl = bannerUrl;
        this.rating = rating;
        this.status = status;
    }

    public Long getMovieId() {
        return movieId;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public String getLanguage() {
        return language;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public Double getRating() {
        return rating;
    }

    public MovieStatus getStatus() {
        return status;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public void setStatus(MovieStatus status) {
        this.status = status;
    }

    public static MovieResponseBuilder builder() {
        return new MovieResponseBuilder();
    }

    public static class MovieResponseBuilder {
        private Long movieId;
        private String title;
        private String genre;
        private Integer durationMinutes;
        private String language;
        private String description;
        private LocalDate releaseDate;
        private String posterUrl;
        private String bannerUrl;
        private Double rating;
        private MovieStatus status;

        public MovieResponseBuilder movieId(Long movieId) {
            this.movieId = movieId;
            return this;
        }

        public MovieResponseBuilder title(String title) {
            this.title = title;
            return this;
        }

        public MovieResponseBuilder genre(String genre) {
            this.genre = genre;
            return this;
        }

        public MovieResponseBuilder durationMinutes(Integer durationMinutes) {
            this.durationMinutes = durationMinutes;
            return this;
        }

        public MovieResponseBuilder language(String language) {
            this.language = language;
            return this;
        }

        public MovieResponseBuilder description(String description) {
            this.description = description;
            return this;
        }

        public MovieResponseBuilder releaseDate(LocalDate releaseDate) {
            this.releaseDate = releaseDate;
            return this;
        }

        public MovieResponseBuilder posterUrl(String posterUrl) {
            this.posterUrl = posterUrl;
            return this;
        }

        public MovieResponseBuilder bannerUrl(String bannerUrl) {
            this.bannerUrl = bannerUrl;
            return this;
        }

        public MovieResponseBuilder rating(Double rating) {
            this.rating = rating;
            return this;
        }

        public MovieResponseBuilder status(MovieStatus status) {
            this.status = status;
            return this;
        }

        public MovieResponse build() {
            return new MovieResponse(this.movieId, this.title, this.genre, this.durationMinutes, this.language, this.description, this.releaseDate, this.posterUrl, this.bannerUrl, this.rating, this.status);
        }
    }
}
