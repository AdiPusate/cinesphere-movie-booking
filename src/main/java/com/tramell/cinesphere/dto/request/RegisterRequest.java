package com.tramell.cinesphere.dto.request;

public class RegisterRequest {
    @jakarta.validation.constraints.NotBlank(message = "Name is required")
    private String name;

    @jakarta.validation.constraints.NotBlank(message = "Email is required")
    @jakarta.validation.constraints.Email(message = "Invalid email format")
    private String email;

    @jakarta.validation.constraints.NotBlank(message = "Password is required")
    @jakarta.validation.constraints.Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @jakarta.validation.constraints.NotBlank(message = "OTP Code is required")
    private String otpCode;

    public RegisterRequest() {
    }

    public RegisterRequest(String name, String email, String password, String otpCode) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.otpCode = otpCode;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getOtpCode() {
        return otpCode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }

    public static RegisterRequestBuilder builder() {
        return new RegisterRequestBuilder();
    }

    public static class RegisterRequestBuilder {
        private String name;
        private String email;
        private String password;
        private String otpCode;

        public RegisterRequestBuilder name(String name) {
            this.name = name;
            return this;
        }

        public RegisterRequestBuilder email(String email) {
            this.email = email;
            return this;
        }

        public RegisterRequestBuilder password(String password) {
            this.password = password;
            return this;
        }

        public RegisterRequestBuilder otpCode(String otpCode) {
            this.otpCode = otpCode;
            return this;
        }

        public RegisterRequest build() {
            return new RegisterRequest(this.name, this.email, this.password, this.otpCode);
        }
    }
}
