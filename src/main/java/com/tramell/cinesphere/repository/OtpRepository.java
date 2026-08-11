package com.tramell.cinesphere.repository;

import com.tramell.cinesphere.entity.OtpEntity;
import com.tramell.cinesphere.enums.OtpPurpose;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<OtpEntity, Long> {
    Optional<OtpEntity> findByEmailAndPurposeAndOtpCode(String email, OtpPurpose purpose, String otpCode);
    void deleteByEmailAndPurpose(String email, OtpPurpose purpose);
}
