package com.tramell.cinesphere.exception;

public class CouponExpiredException extends RuntimeException {
    public CouponExpiredException(String message) {
        super(message);
    }
}
