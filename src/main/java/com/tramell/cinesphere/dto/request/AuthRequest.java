package com.tramell.cinesphere.dto.request;

public class AuthRequest {
    @jakarta.validation.constraints.NotBlank(message = "Email is required")
    @jakarta.validation.constraints.Email(message = "Invalid email format")
    private String email;

    @jakarta.validation.constraints.NotBlank(message = "Password is required")
    private String password;

    public AuthRequest() {
    }

    public AuthRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static AuthRequestBuilder builder() {
        return new AuthRequestBuilder();
    }

    public static class AuthRequestBuilder {
        private String email;
        private String password;

        public AuthRequestBuilder email(String email) {
            this.email = email;
            return this;
        }

        public AuthRequestBuilder password(String password) {
            this.password = password;
            return this;
        }

        public AuthRequest build() {
            return new AuthRequest(this.email, this.password);
        }
    }
}
