package com.tramell.cinesphere.service;

import com.tramell.cinesphere.dto.request.CouponRequest;
import com.tramell.cinesphere.dto.response.CouponResponse;

import java.util.List;

public interface CouponService {
    CouponResponse addCoupon(CouponRequest request);
    CouponResponse validateCoupon(String code);
    List<CouponResponse> getActiveCoupons();
    void deleteCoupon(Long couponId);
}
