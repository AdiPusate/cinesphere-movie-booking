package com.tramell.cinesphere;

import com.tramell.cinesphere.entity.User;
import com.tramell.cinesphere.enums.Role;
import com.tramell.cinesphere.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class CinesphereApplication {

    public static void main(String[] args) {
        SpringApplication.run(CinesphereApplication.class, args);
    }

    @Bean
    public CommandLineRunner seedDemoUsers(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.seed-demo-users:true}") boolean seedDemoUsers,
            @Value("${app.demo-admin-email:Admin@cinesphere.com}") String adminEmail,
            @Value("${app.demo-admin-password:admin}") String adminPassword,
            @Value("${app.demo-customer-email:user@cinesphere.com}") String customerEmail,
            @Value("${app.demo-customer-password:user}") String customerPassword) {

        return args -> {
            if (!seedDemoUsers) {
                return;
            }

            if (!userRepository.existsByEmail(adminEmail)) {
                userRepository.save(User.builder()
                        .name("Admin")
                        .email(adminEmail.trim().toLowerCase())
                        .password(passwordEncoder.encode(adminPassword))
                        .role(Role.ADMIN)
                        .build());
            }

            if (!userRepository.existsByEmail(customerEmail)) {
                userRepository.save(User.builder()
                        .name("User")
                        .email(customerEmail.trim().toLowerCase())
                        .password(passwordEncoder.encode(customerPassword))
                        .role(Role.CUSTOMER)
                        .build());
            }
        };
    }
}
