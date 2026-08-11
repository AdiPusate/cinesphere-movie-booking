package com.tramell.cinesphere.service.impl;

import com.tramell.cinesphere.dto.request.CouponRequest;
import com.tramell.cinesphere.dto.response.CouponResponse;
import com.tramell.cinesphere.entity.Coupon;
import com.tramell.cinesphere.exception.CouponExpiredException;
import com.tramell.cinesphere.exception.ResourceNotFoundException;
import com.tramell.cinesphere.mapper.CouponMapper;
import com.tramell.cinesphere.repository.CouponRepository;
import com.tramell.cinesphere.service.CouponService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Locale;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final CouponMapper couponMapper;

    @Override
    @Transactional
    public CouponResponse addCoupon(CouponRequest request) {
        Coupon coupon = Coupon.builder()
                .code(request.getCode().trim().toUpperCase(Locale.ROOT))
                .discountPercentage(request.getDiscountPercentage())
                .expiryDate(request.getExpiryDate())
                .isActive(request.getIsActive() == null || request.getIsActive())
                .build();
        Coupon savedCoupon = couponRepository.save(coupon);
        return couponMapper.toResponse(savedCoupon);
    }

    @Override
    public CouponResponse validateCoupon(String code) {
        String normalizedCode = code == null ? "" : code.trim().toUpperCase(Locale.ROOT);
        Coupon coupon = couponRepository.findByCode(normalizedCode)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon", "code", normalizedCode));

        if (!Boolean.TRUE.equals(coupon.getIsActive())
                || coupon.getExpiryDate() == null
                || coupon.getExpiryDate().isBefore(LocalDate.now())) {
            throw new CouponExpiredException("Coupon is not active or has expired");
        }

        return couponMapper.toResponse(coupon);
    }

    @Override
    public List<CouponResponse> getActiveCoupons() {
        return couponRepository.findByIsActiveTrue().stream()
                .filter(coupon -> !coupon.getExpiryDate().isBefore(LocalDate.now()))
                .map(couponMapper::toResponse)
                .collect(Collectors.toList());
    }

    public CouponServiceImpl(CouponRepository couponRepository, CouponMapper couponMapper) {
        this.couponRepository = couponRepository;
        this.couponMapper = couponMapper;
    }

    @Override
    @Transactional
    public void deleteCoupon(Long couponId) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon", "id", couponId));
        couponRepository.delete(coupon);
    }
}
