package com.tramell.cinesphere.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "theatres")
public class Theatre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "theatre_id")
    private Long theatreId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String address;

    @OneToMany(mappedBy = "theatre", cascade = CascadeType.ALL)
private List<Show> shows = new ArrayList<>();

    public Theatre() {
    }

    public Theatre(Long theatreId, String name, String city, String address, List<Show> shows) {
        this.theatreId = theatreId;
        this.name = name;
        this.city = city;
        this.address = address;
        this.shows = shows;
    }

    public Long getTheatreId() {
        return theatreId;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public List<Show> getShows() {
        return shows;
    }

    public void setTheatreId(Long theatreId) {
        this.theatreId = theatreId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setShows(List<Show> shows) {
        this.shows = shows;
    }

    public static TheatreBuilder builder() {
        return new TheatreBuilder();
    }

    public static class TheatreBuilder {
        private Long theatreId;
        private String name;
        private String city;
        private String address;
        private List<Show> shows;

        public TheatreBuilder theatreId(Long theatreId) {
            this.theatreId = theatreId;
            return this;
        }

        public TheatreBuilder name(String name) {
            this.name = name;
            return this;
        }

        public TheatreBuilder city(String city) {
            this.city = city;
            return this;
        }

        public TheatreBuilder address(String address) {
            this.address = address;
            return this;
        }

        public TheatreBuilder shows(List<Show> shows) {
            this.shows = shows;
            return this;
        }

        public Theatre build() {
            return new Theatre(this.theatreId, this.name, this.city, this.address, this.shows);
        }
    }
}
