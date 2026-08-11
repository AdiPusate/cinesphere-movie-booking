package com.tramell.cinesphere.dto.response;

import com.tramell.cinesphere.enums.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class BookingResponse {
    private Long bookingId;
    private ShowResponse show;
    private BigDecimal totalAmount;
    private BookingStatus bookingStatus;
    private List<String> bookedSeats;
    private LocalDateTime bookingDate;
    private String receiptId;
    private String receiptUrl;

    public BookingResponse() {
    }

    public BookingResponse(Long bookingId, ShowResponse show, BigDecimal totalAmount, BookingStatus bookingStatus, List<String> bookedSeats, LocalDateTime bookingDate, String receiptId, String receiptUrl) {
        this.bookingId = bookingId;
        this.show = show;
        this.totalAmount = totalAmount;
        this.bookingStatus = bookingStatus;
        this.bookedSeats = bookedSeats;
        this.bookingDate = bookingDate;
        this.receiptId = receiptId;
        this.receiptUrl = receiptUrl;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public ShowResponse getShow() {
        return show;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    public List<String> getBookedSeats() {
        return bookedSeats;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public String getReceiptId() {
        return receiptId;
    }

    public String getReceiptUrl() {
        return receiptUrl;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public void setShow(ShowResponse show) {
        this.show = show;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setBookingStatus(BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public void setBookedSeats(List<String> bookedSeats) {
        this.bookedSeats = bookedSeats;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }

    public void setReceiptId(String receiptId) {
        this.receiptId = receiptId;
    }

    public void setReceiptUrl(String receiptUrl) {
        this.receiptUrl = receiptUrl;
    }

    public static BookingResponseBuilder builder() {
        return new BookingResponseBuilder();
    }

    public static class BookingResponseBuilder {
        private Long bookingId;
        private ShowResponse show;
        private BigDecimal totalAmount;
        private BookingStatus bookingStatus;
        private List<String> bookedSeats;
        private LocalDateTime bookingDate;
        private String receiptId;
        private String receiptUrl;

        public BookingResponseBuilder bookingId(Long bookingId) {
            this.bookingId = bookingId;
            return this;
        }

        public BookingResponseBuilder show(ShowResponse show) {
            this.show = show;
            return this;
        }

        public BookingResponseBuilder totalAmount(BigDecimal totalAmount) {
            this.totalAmount = totalAmount;
            return this;
        }

        public BookingResponseBuilder bookingStatus(BookingStatus bookingStatus) {
            this.bookingStatus = bookingStatus;
            return this;
        }

        public BookingResponseBuilder bookedSeats(List<String> bookedSeats) {
            this.bookedSeats = bookedSeats;
            return this;
        }

        public BookingResponseBuilder bookingDate(LocalDateTime bookingDate) {
            this.bookingDate = bookingDate;
            return this;
        }

        public BookingResponseBuilder receiptId(String receiptId) {
            this.receiptId = receiptId;
            return this;
        }

        public BookingResponseBuilder receiptUrl(String receiptUrl) {
            this.receiptUrl = receiptUrl;
            return this;
        }

        public BookingResponse build() {
            return new BookingResponse(this.bookingId, this.show, this.totalAmount, this.bookingStatus, this.bookedSeats, this.bookingDate, this.receiptId, this.receiptUrl);
        }
    }
}
