package com.tramell.cinesphere.dto.request;

import jakarta.validation.constraints.Size;

public class UserUpdateRequest {
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    public UserUpdateRequest() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
