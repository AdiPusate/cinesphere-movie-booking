package com.tramell.cinesphere.dto.response;

import com.tramell.cinesphere.enums.Role;

public class UserResponse {
    private Long userId;
    private String name;
    private String email;
    private Role role;

    public UserResponse() {
    }

    public UserResponse(Long userId, String name, String email, Role role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public Long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public static UserResponseBuilder builder() {
        return new UserResponseBuilder();
    }

    public static class UserResponseBuilder {
        private Long userId;
        private String name;
        private String email;
        private Role role;

        public UserResponseBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public UserResponseBuilder name(String name) {
            this.name = name;
            return this;
        }

        public UserResponseBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserResponseBuilder role(Role role) {
            this.role = role;
            return this;
        }

        public UserResponse build() {
            return new UserResponse(this.userId, this.name, this.email, this.role);
        }
    }
}
