package com.tramell.cinesphere.dto.response;

import com.tramell.cinesphere.enums.SeatStatus;

public class ShowSeatResponse {
    private Long seatId;
    private String seatNumber;
    private SeatStatus status;

    public ShowSeatResponse() {
    }

    public ShowSeatResponse(Long seatId, String seatNumber, SeatStatus status) {
        this.seatId = seatId;
        this.seatNumber = seatNumber;
        this.status = status;
    }

    public Long getSeatId() {
        return seatId;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public SeatStatus getStatus() {
        return status;
    }

    public void setSeatId(Long seatId) {
        this.seatId = seatId;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public void setStatus(SeatStatus status) {
        this.status = status;
    }

    public static ShowSeatResponseBuilder builder() {
        return new ShowSeatResponseBuilder();
    }

    public static class ShowSeatResponseBuilder {
        private Long seatId;
        private String seatNumber;
        private SeatStatus status;

        public ShowSeatResponseBuilder seatId(Long seatId) {
            this.seatId = seatId;
            return this;
        }

        public ShowSeatResponseBuilder seatNumber(String seatNumber) {
            this.seatNumber = seatNumber;
            return this;
        }

        public ShowSeatResponseBuilder status(SeatStatus status) {
            this.status = status;
            return this;
        }

        public ShowSeatResponse build() {
            return new ShowSeatResponse(this.seatId, this.seatNumber, this.status);
        }
    }
}
