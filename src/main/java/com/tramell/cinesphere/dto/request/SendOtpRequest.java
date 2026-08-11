package com.tramell.cinesphere.dto.request;

import com.tramell.cinesphere.enums.OtpPurpose;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SendOtpRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "Purpose is required")
    private OtpPurpose purpose;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public OtpPurpose getPurpose() {
        return purpose;
    }

    public void setPurpose(OtpPurpose purpose) {
        this.purpose = purpose;
    }
}
