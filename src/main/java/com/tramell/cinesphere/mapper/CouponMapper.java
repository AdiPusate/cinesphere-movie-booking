package com.tramell.cinesphere.mapper;

import com.tramell.cinesphere.dto.response.CouponResponse;
import com.tramell.cinesphere.entity.Coupon;
import org.springframework.stereotype.Component;

@Component
public class CouponMapper {
    public CouponResponse toResponse(Coupon coupon) {
        if (coupon == null) return null;
        return CouponResponse.builder()
                .couponId(coupon.getCouponId())
                .code(coupon.getCode())
                .discountPercentage(coupon.getDiscountPercentage())
                .expiryDate(coupon.getExpiryDate())
                .isActive(coupon.getIsActive())
                .build();
    }
}
