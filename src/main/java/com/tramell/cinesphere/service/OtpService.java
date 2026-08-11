package com.tramell.cinesphere.service;

import com.tramell.cinesphere.entity.OtpEntity;
import com.tramell.cinesphere.enums.OtpPurpose;
import com.tramell.cinesphere.exception.BadRequestException;
import com.tramell.cinesphere.repository.OtpRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class OtpService {

    private final OtpRepository otpRepository;
    private final Random random = new Random();

    public OtpService(OtpRepository otpRepository) {
        this.otpRepository = otpRepository;
    }

    @Transactional
    public void generateAndSendOtp(String email, OtpPurpose purpose) {
        otpRepository.deleteByEmailAndPurpose(email, purpose);

        String otpCode = String.format("%06d", random.nextInt(999999));
        
        OtpEntity otpEntity = new OtpEntity(
                email,
                otpCode,
                purpose,
                LocalDateTime.now().plusMinutes(10) // 10 minutes expiry
        );
        
        otpRepository.save(otpEntity);

        // MOCK EMAIL SENDING
        System.out.println("=================================================");
        System.out.println("MOCK EMAIL SENT TO: " + email);
        System.out.println("PURPOSE: " + purpose);
        System.out.println("YOUR OTP CODE IS: " + otpCode);
        System.out.println("=================================================");
    }

    @Transactional
    public void validateOtp(String email, OtpPurpose purpose, String otpCode) {
        OtpEntity otpEntity = otpRepository.findByEmailAndPurposeAndOtpCode(email, purpose, otpCode)
                .orElseThrow(() -> new BadRequestException("Invalid or expired OTP"));

        if (otpEntity.getExpiresAt().isBefore(LocalDateTime.now())) {
            otpRepository.delete(otpEntity);
            throw new BadRequestException("OTP has expired");
        }

        // Once validated, delete it so it can't be reused
        otpRepository.delete(otpEntity);
    }
}
