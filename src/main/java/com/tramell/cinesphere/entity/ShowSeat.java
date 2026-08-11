package com.tramell.cinesphere.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "show_seats", uniqueConstraints = {
        @UniqueConstraint(name = "uk_show_seat_number", columnNames = {"show_id", "seat_number"})
})
public class ShowSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id")
    private Long seatId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "show_id", nullable = false)
    private Show show;

    @Column(name = "seat_number", nullable = false)
    private String seatNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private com.tramell.cinesphere.enums.SeatStatus status; // e.g., AVAILABLE, BOOKED

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private Booking booking;

    public ShowSeat() {
    }

    public ShowSeat(Long seatId, Show show, String seatNumber, com.tramell.cinesphere.enums.SeatStatus status, Booking booking) {
        this.seatId = seatId;
        this.show = show;
        this.seatNumber = seatNumber;
        this.status = status;
        this.booking = booking;
    }

    public Long getSeatId() {
        return seatId;
    }

    public Show getShow() {
        return show;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public com.tramell.cinesphere.enums.SeatStatus getStatus() {
        return status;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setSeatId(Long seatId) {
        this.seatId = seatId;
    }

    public void setShow(Show show) {
        this.show = show;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public void setStatus(com.tramell.cinesphere.enums.SeatStatus status) {
        this.status = status;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public static ShowSeatBuilder builder() {
        return new ShowSeatBuilder();
    }

    public static class ShowSeatBuilder {
        private Long seatId;
        private Show show;
        private String seatNumber;
        private com.tramell.cinesphere.enums.SeatStatus status;
        private Booking booking;

        public ShowSeatBuilder seatId(Long seatId) {
            this.seatId = seatId;
            return this;
        }

        public ShowSeatBuilder show(Show show) {
            this.show = show;
            return this;
        }

        public ShowSeatBuilder seatNumber(String seatNumber) {
            this.seatNumber = seatNumber;
            return this;
        }

        public ShowSeatBuilder status(com.tramell.cinesphere.enums.SeatStatus status) {
            this.status = status;
            return this;
        }

        public ShowSeatBuilder booking(Booking booking) {
            this.booking = booking;
            return this;
        }

        public ShowSeat build() {
            return new ShowSeat(this.seatId, this.show, this.seatNumber, this.status, this.booking);
        }
    }
}
