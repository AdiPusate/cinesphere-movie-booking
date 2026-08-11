package com.tramell.cinesphere.entity;

import jakarta.persistence.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private com.tramell.cinesphere.enums.Role role; // e.g., ADMIN, CUSTOMER

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
private List<Booking> bookings = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public User() {
    }

    public User(Long userId, String name, String email, String password, com.tramell.cinesphere.enums.Role role, List<Booking> bookings) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.bookings = bookings;
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

    public String getPassword() {
        return password;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public com.tramell.cinesphere.enums.Role getRole() {
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

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public void setRole(com.tramell.cinesphere.enums.Role role) {
        this.role = role;
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public static class UserBuilder {
        private Long userId;
        private String name;
        private String email;
        private String password;
        private com.tramell.cinesphere.enums.Role role;
        private List<Booking> bookings;

        public UserBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public UserBuilder name(String name) {
            this.name = name;
            return this;
        }

        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder bookings(List<Booking> bookings) {
            this.bookings = bookings;
            return this;
        }

        public UserBuilder role(com.tramell.cinesphere.enums.Role role) {
            this.role = role;
            return this;
        }

        public User build() {
            return new User(this.userId, this.name, this.email, this.password, this.role, this.bookings);
        }
    }
}
