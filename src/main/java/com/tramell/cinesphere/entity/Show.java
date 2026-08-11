package com.tramell.cinesphere.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "shows")
public class Show {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "show_id")
    private Long showId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theatre_id", nullable = false)
    private Theatre theatre;

    @Column(name = "show_time", nullable = false)
    private LocalDateTime showTime;

    @Column(name = "base_price", nullable = false)
    private BigDecimal basePrice;

    @OneToMany(mappedBy = "show", cascade = CascadeType.ALL)
private List<ShowSeat> showSeats = new ArrayList<>();

    @OneToMany(mappedBy = "show", cascade = CascadeType.ALL)
private List<Booking> bookings = new ArrayList<>();

    public Show() {
    }

    public Show(Long showId, Movie movie, Theatre theatre, LocalDateTime showTime, BigDecimal basePrice, List<ShowSeat> showSeats, List<Booking> bookings) {
        this.showId = showId;
        this.movie = movie;
        this.theatre = theatre;
        this.showTime = showTime;
        this.basePrice = basePrice;
        this.showSeats = showSeats;
        this.bookings = bookings;
    }

    public Long getShowId() {
        return showId;
    }

    public Movie getMovie() {
        return movie;
    }

    public Theatre getTheatre() {
        return theatre;
    }

    public LocalDateTime getShowTime() {
        return showTime;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public List<ShowSeat> getShowSeats() {
        return showSeats;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setShowId(Long showId) {
        this.showId = showId;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public void setTheatre(Theatre theatre) {
        this.theatre = theatre;
    }

    public void setShowTime(LocalDateTime showTime) {
        this.showTime = showTime;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public void setShowSeats(List<ShowSeat> showSeats) {
        this.showSeats = showSeats;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public static ShowBuilder builder() {
        return new ShowBuilder();
    }

    public static class ShowBuilder {
        private Long showId;
        private Movie movie;
        private Theatre theatre;
        private LocalDateTime showTime;
        private BigDecimal basePrice;
        private List<ShowSeat> showSeats;
        private List<Booking> bookings;

        public ShowBuilder showId(Long showId) {
            this.showId = showId;
            return this;
        }

        public ShowBuilder movie(Movie movie) {
            this.movie = movie;
            return this;
        }

        public ShowBuilder theatre(Theatre theatre) {
            this.theatre = theatre;
            return this;
        }

        public ShowBuilder showTime(LocalDateTime showTime) {
            this.showTime = showTime;
            return this;
        }

        public ShowBuilder basePrice(BigDecimal basePrice) {
            this.basePrice = basePrice;
            return this;
        }

        public ShowBuilder showSeats(List<ShowSeat> showSeats) {
            this.showSeats = showSeats;
            return this;
        }

        public ShowBuilder bookings(List<Booking> bookings) {
            this.bookings = bookings;
            return this;
        }

        public Show build() {
            return new Show(this.showId, this.movie, this.theatre, this.showTime, this.basePrice, this.showSeats, this.bookings);
        }
    }
}
