package com.tramell.cinesphere.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_id")
    private Long movieId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String genre;

    @Column(name = "duration", nullable = false)
    private Integer duration;

    @Column(nullable = false)
    private String language;

    @Column(length = 1000)
    private String description;

    @Column(name = "release_date")
    private java.time.LocalDate releaseDate;

    @Column(name = "banner_url")
    private String bannerUrl;

    private Double rating;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
private com.tramell.cinesphere.enums.MovieStatus status = com.tramell.cinesphere.enums.MovieStatus.NOW_SHOWING;
    
    private String posterUrl;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<Show> shows = new ArrayList<>();

    public Movie() {
    }

    public Movie(Long movieId, String title, String genre, Integer duration, String language, String description, java.time.LocalDate releaseDate, String bannerUrl, Double rating, com.tramell.cinesphere.enums.MovieStatus status, String posterUrl, List<Show> shows) {
        this.movieId = movieId;
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.language = language;
        this.description = description;
        this.releaseDate = releaseDate;
        this.bannerUrl = bannerUrl;
        this.rating = rating;
        this.status = status;
        this.posterUrl = posterUrl;
        this.shows = shows;
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

    public Integer getDuration() {
        return duration;
    }

    public String getLanguage() {
        return language;
    }

    public String getDescription() {
        return description;
    }

    public java.time.LocalDate getReleaseDate() {
        return releaseDate;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public Double getRating() {
        return rating;
    }

    public com.tramell.cinesphere.enums.MovieStatus getStatus() {
        return status;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public List<Show> getShows() {
        return shows;
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

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setReleaseDate(java.time.LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public void setStatus(com.tramell.cinesphere.enums.MovieStatus status) {
        this.status = status;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public void setShows(List<Show> shows) {
        this.shows = shows;
    }

    public static MovieBuilder builder() {
        return new MovieBuilder();
    }

    public static class MovieBuilder {
        private Long movieId;
        private String title;
        private String genre;
        private Integer duration;
        private String language;
        private String description;
        private java.time.LocalDate releaseDate;
        private String bannerUrl;
        private Double rating;
        private com.tramell.cinesphere.enums.MovieStatus status;
        private String posterUrl;
        private List<Show> shows;

        public MovieBuilder movieId(Long movieId) {
            this.movieId = movieId;
            return this;
        }

        public MovieBuilder title(String title) {
            this.title = title;
            return this;
        }

        public MovieBuilder genre(String genre) {
            this.genre = genre;
            return this;
        }

        public MovieBuilder duration(Integer duration) {
            this.duration = duration;
            return this;
        }

        public MovieBuilder language(String language) {
            this.language = language;
            return this;
        }

        public MovieBuilder description(String description) {
            this.description = description;
            return this;
        }

        public MovieBuilder releaseDate(java.time.LocalDate releaseDate) {
            this.releaseDate = releaseDate;
            return this;
        }

        public MovieBuilder bannerUrl(String bannerUrl) {
            this.bannerUrl = bannerUrl;
            return this;
        }

        public MovieBuilder rating(Double rating) {
            this.rating = rating;
            return this;
        }

        public MovieBuilder status(com.tramell.cinesphere.enums.MovieStatus status) {
            this.status = status;
            return this;
        }

        public MovieBuilder posterUrl(String posterUrl) {
            this.posterUrl = posterUrl;
            return this;
        }

        public MovieBuilder shows(List<Show> shows) {
            this.shows = shows;
            return this;
        }

        public Movie build() {
            return new Movie(this.movieId, this.title, this.genre, this.duration, this.language, this.description, this.releaseDate, this.bannerUrl, this.rating, this.status, this.posterUrl, this.shows);
        }
    }
}
