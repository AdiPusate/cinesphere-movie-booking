package com.tramell.cinesphere.controller;

import com.tramell.cinesphere.dto.ApiResponse;
import com.tramell.cinesphere.dto.request.CouponRequest;
import com.tramell.cinesphere.dto.response.CouponResponse;
import com.tramell.cinesphere.service.CouponService;
import com.tramell.cinesphere.util.ApiResponseUtil;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tramell/cinesphere/coupons")
public class CouponController {

    private final CouponService couponService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CouponResponse>> addCoupon(@Valid @RequestBody CouponRequest request) {
        return ApiResponseUtil.created(couponService.addCoupon(request), "Coupon added successfully");
    }

    @GetMapping("/validate/{code}")
    public ResponseEntity<ApiResponse<CouponResponse>> validateCoupon(@PathVariable String code) {
        return ApiResponseUtil.success(couponService.validateCoupon(code), "Coupon is valid");
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CouponResponse>>> getActiveCoupons() {
        return ApiResponseUtil.success(couponService.getActiveCoupons());
    }

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteCoupon(@PathVariable Long id) {
        couponService.deleteCoupon(id);
        return ApiResponseUtil.success(null, "Coupon deleted successfully");
    }
}
