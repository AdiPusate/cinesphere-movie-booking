package com.tramell.cinesphere.dto.request;

import com.tramell.cinesphere.enums.MovieStatus;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public class MovieRequest {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Genre is required")
    private String genre;

    @NotNull(message = "Duration is required")
    @Positive(message = "Duration must be positive")
    private Integer durationMinutes;

    @NotBlank(message = "Language is required")
    private String language;

    private String description;
    
    private LocalDate releaseDate;
    
    private String posterUrl;
    
    private String bannerUrl;
    
    @DecimalMin(value = "0.0", message = "Rating cannot be negative")
    @DecimalMax(value = "10.0", message = "Rating cannot exceed 10")
    private Double rating;

    @NotNull(message = "Status is required")
    private MovieStatus status;

    public MovieRequest() {
    }

    public MovieRequest(String title, String genre, Integer durationMinutes, String language, String description, LocalDate releaseDate, String posterUrl, String bannerUrl, Double rating, MovieStatus status) {
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
}
