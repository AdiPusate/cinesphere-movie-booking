package com.tramell.cinesphere.service;

import com.tramell.cinesphere.dto.request.AuthRequest;
import com.tramell.cinesphere.dto.request.RegisterRequest;
import com.tramell.cinesphere.dto.request.SendOtpRequest;
import com.tramell.cinesphere.dto.request.ResetPasswordRequest;
import com.tramell.cinesphere.dto.response.AuthResponse;
import com.tramell.cinesphere.dto.response.UserResponse;
import com.tramell.cinesphere.entity.User;
import com.tramell.cinesphere.exception.BadRequestException;
import com.tramell.cinesphere.enums.Role;
import com.tramell.cinesphere.repository.UserRepository;
import com.tramell.cinesphere.security.JwtService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class AuthService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final OtpService otpService;

    public void sendOtp(SendOtpRequest request) {
        String email = request.getEmail().trim().toLowerCase(Locale.ROOT);
        if (request.getPurpose() == com.tramell.cinesphere.enums.OtpPurpose.REGISTER) {
            if (repository.existsByEmail(email)) {
                throw new BadRequestException("Email already in use");
            }
        } else if (request.getPurpose() == com.tramell.cinesphere.enums.OtpPurpose.FORGOT_PASSWORD) {
            if (!repository.existsByEmail(email)) {
                throw new BadRequestException("Email not found");
            }
        }
        otpService.generateAndSendOtp(email, request.getPurpose());
    }

    public void resetPassword(ResetPasswordRequest request) {
        String email = request.getEmail().trim().toLowerCase(Locale.ROOT);
        otpService.validateOtp(email, com.tramell.cinesphere.enums.OtpPurpose.FORGOT_PASSWORD, request.getOtpCode());
        
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("User not found"));
        
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        repository.save(user);
    }

    public AuthResponse register(RegisterRequest request) {
        String email = request.getEmail().trim().toLowerCase(Locale.ROOT);
        if (repository.existsByEmail(email)) {
            throw new BadRequestException("Email already in use");
        }

        otpService.validateOtp(email, com.tramell.cinesphere.enums.OtpPurpose.REGISTER, request.getOtpCode());

        var user = User.builder()
                .name(request.getName().trim())
                .email(email)
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.CUSTOMER) // Defaulting to CUSTOMER for presentation simplicity
                .build();
        user = repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        
        var userResponse = UserResponse.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();

        return AuthResponse.builder()
                .token(jwtToken)
                .user(userResponse)
                .build();
    }

    public AuthResponse authenticate(AuthRequest request) {
        String email = request.getEmail().trim().toLowerCase(Locale.ROOT);
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email,
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("Invalid credentials"));
        var jwtToken = jwtService.generateToken(user);

        var userResponse = UserResponse.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();

        return AuthResponse.builder()
                .token(jwtToken)
                .user(userResponse)
                .build();
    }

    public AuthService(UserRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager, OtpService otpService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.otpService = otpService;
    }
}
