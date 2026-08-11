package com.tramell.cinesphere.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class CouponRequest {
    @NotBlank(message = "Coupon code is required")
    @Pattern(regexp = "^[A-Za-z0-9_-]{3,30}$", message = "Coupon code must be 3-30 letters, numbers, _ or -")
    private String code;

    @NotNull(message = "Discount percentage is required")
    @Min(value = 1, message = "Discount must be at least 1%")
    @Max(value = 100, message = "Discount cannot exceed 100%")
    private Integer discountPercentage;

    @NotNull(message = "Expiry date is required")
    @Future(message = "Expiry date must be in the future")
    private LocalDate expiryDate;

    private Boolean isActive = true;

    public CouponRequest() {
    }

    public CouponRequest(String code, Integer discountPercentage, LocalDate expiryDate, Boolean isActive) {
        this.code = code;
        this.discountPercentage = discountPercentage;
        this.expiryDate = expiryDate;
        this.isActive = isActive;
    }

    public String getCode() {
        return code;
    }

    public Integer getDiscountPercentage() {
        return discountPercentage;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDiscountPercentage(Integer discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
