package com.tramell.cinesphere.entity;

import com.tramell.cinesphere.enums.OtpPurpose;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "otps")
public class OtpEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, length = 6)
    private String otpCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OtpPurpose purpose;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    public OtpEntity() {
    }

    public OtpEntity(String email, String otpCode, OtpPurpose purpose, LocalDateTime expiresAt) {
        this.email = email;
        this.otpCode = otpCode;
        this.purpose = purpose;
        this.expiresAt = expiresAt;
    }

    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getOtpCode() { return otpCode; }
    public OtpPurpose getPurpose() { return purpose; }
    public LocalDateTime getExpiresAt() { return expiresAt; }

    public void setId(Long id) { this.id = id; }
    public void setEmail(String email) { this.email = email; }
    public void setOtpCode(String otpCode) { this.otpCode = otpCode; }
    public void setPurpose(OtpPurpose purpose) { this.purpose = purpose; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
}
