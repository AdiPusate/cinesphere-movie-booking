package com.tramell.cinesphere.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public class BookingRequest {
    @NotNull(message = "Show ID is required")
    private Long showId;

    @NotEmpty(message = "At least one seat must be selected")
    @Size(max = 10, message = "Maximum 10 seats allowed per booking")
    private List<@Pattern(
            regexp = "^[A-Z]{1,2}[0-9]{1,2}$",
            message = "Invalid seat number") String> seatNumbers;

    private String couponCode;

    public BookingRequest() {
    }

    public BookingRequest(Long showId, List<String> seatNumbers, String couponCode) {
        this.showId = showId;
        this.seatNumbers = seatNumbers;
        this.couponCode = couponCode;
    }

    public Long getShowId() {
        return showId;
    }

    public List<String> getSeatNumbers() {
        return seatNumbers;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setShowId(Long showId) {
        this.showId = showId;
    }

    public void setSeatNumbers(List<String> seatNumbers) {
        this.seatNumbers = seatNumbers;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }
}
