package com.tramell.cinesphere.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "coupons")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long couponId;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(name = "discount_percentage", nullable = false)
    private Integer discountPercentage;

    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @OneToMany(mappedBy = "coupon")
private List<Booking> bookings = new ArrayList<>();

    public Coupon() {
    }

    public Coupon(Long couponId, String code, Integer discountPercentage, LocalDate expiryDate, Boolean isActive, List<Booking> bookings) {
        this.couponId = couponId;
        this.code = code;
        this.discountPercentage = discountPercentage;
        this.expiryDate = expiryDate;
        this.isActive = isActive;
        this.bookings = bookings;
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

    public Boolean getIsActive() {
        return isActive;
    }

    public List<Booking> getBookings() {
        return bookings;
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

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public static CouponBuilder builder() {
        return new CouponBuilder();
    }

    public static class CouponBuilder {
        private Long couponId;
        private String code;
        private Integer discountPercentage;
        private LocalDate expiryDate;
        private Boolean isActive;
        private List<Booking> bookings;

        public CouponBuilder couponId(Long couponId) {
            this.couponId = couponId;
            return this;
        }

        public CouponBuilder code(String code) {
            this.code = code;
            return this;
        }

        public CouponBuilder discountPercentage(Integer discountPercentage) {
            this.discountPercentage = discountPercentage;
            return this;
        }

        public CouponBuilder expiryDate(LocalDate expiryDate) {
            this.expiryDate = expiryDate;
            return this;
        }

        public CouponBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public CouponBuilder bookings(List<Booking> bookings) {
            this.bookings = bookings;
            return this;
        }

        public Coupon build() {
            return new Coupon(this.couponId, this.code, this.discountPercentage, this.expiryDate, this.isActive, this.bookings);
        }
    }
}
