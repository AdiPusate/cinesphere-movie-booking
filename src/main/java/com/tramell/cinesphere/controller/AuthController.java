package com.tramell.cinesphere.controller;

import com.tramell.cinesphere.dto.request.AuthRequest;
import com.tramell.cinesphere.dto.request.RegisterRequest;
import com.tramell.cinesphere.dto.request.SendOtpRequest;
import com.tramell.cinesphere.dto.request.ResetPasswordRequest;
import com.tramell.cinesphere.dto.response.AuthResponse;
import com.tramell.cinesphere.dto.ApiResponse;
import com.tramell.cinesphere.service.AuthService;

import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tramell/cinesphere/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/send-otp")
    public ResponseEntity<ApiResponse<String>> sendOtp(@Valid @RequestBody SendOtpRequest request) {
        authService.sendOtp(request);
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .success(true)
                .message("OTP sent successfully")
                .data(null)
                .build());
    }

    @PostMapping("/forgot-password/reset")
    public ResponseEntity<ApiResponse<String>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .success(true)
                .message("Password reset successfully")
                .data(null)
                .build());
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    public AuthController(AuthService authService) {
        this.authService = authService;
    }
}
