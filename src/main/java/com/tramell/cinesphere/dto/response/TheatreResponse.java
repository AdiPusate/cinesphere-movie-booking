package com.tramell.cinesphere.dto.response;

public class TheatreResponse {
    private Long theatreId;
    private String name;
    private String city;
    private String address;

    public TheatreResponse() {
    }

    public TheatreResponse(Long theatreId, String name, String city, String address) {
        this.theatreId = theatreId;
        this.name = name;
        this.city = city;
        this.address = address;
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

    public static TheatreResponseBuilder builder() {
        return new TheatreResponseBuilder();
    }

    public static class TheatreResponseBuilder {
        private Long theatreId;
        private String name;
        private String city;
        private String address;

        public TheatreResponseBuilder theatreId(Long theatreId) {
            this.theatreId = theatreId;
            return this;
        }

        public TheatreResponseBuilder name(String name) {
            this.name = name;
            return this;
        }

        public TheatreResponseBuilder city(String city) {
            this.city = city;
            return this;
        }

        public TheatreResponseBuilder address(String address) {
            this.address = address;
            return this;
        }

        public TheatreResponse build() {
            return new TheatreResponse(this.theatreId, this.name, this.city, this.address);
        }
    }
}
