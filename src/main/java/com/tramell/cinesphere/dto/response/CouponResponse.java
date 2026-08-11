package com.tramell.cinesphere.dto.response;

import java.time.LocalDate;

public class CouponResponse {
    private Long couponId;
    private String code;
    private Integer discountPercentage;
    private LocalDate expiryDate;
    private Boolean isActive;

    public CouponResponse() {
    }

    public CouponResponse(Long couponId, String code, Integer discountPercentage, LocalDate expiryDate, Boolean isActive) {
        this.couponId = couponId;
        this.code = code;
        this.discountPercentage = discountPercentage;
        this.expiryDate = expiryDate;
        this.isActive = isActive;
    }

    public Long getCouponId() {
        return couponId;
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

    public Boolean isIsActive() {
        return isActive;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
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

    public static CouponResponseBuilder builder() {
        return new CouponResponseBuilder();
    }

    public static class CouponResponseBuilder {
        private Long couponId;
        private String code;
        private Integer discountPercentage;
        private LocalDate expiryDate;
        private Boolean isActive;

        public CouponResponseBuilder couponId(Long couponId) {
            this.couponId = couponId;
            return this;
        }

        public CouponResponseBuilder code(String code) {
            this.code = code;
            return this;
        }

        public CouponResponseBuilder discountPercentage(Integer discountPercentage) {
            this.discountPercentage = discountPercentage;
            return this;
        }

        public CouponResponseBuilder expiryDate(LocalDate expiryDate) {
            this.expiryDate = expiryDate;
            return this;
        }

        public CouponResponseBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public CouponResponse build() {
            return new CouponResponse(this.couponId, this.code, this.discountPercentage, this.expiryDate, this.isActive);
        }
    }
}
