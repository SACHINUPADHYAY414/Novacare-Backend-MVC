package com.healthcare.config;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.healthcare.model.User;
import com.healthcare.repository.UserRepository;

@Configuration
public class AdminUserInitializer {

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.name}")
    private String adminName;

    @Value("${admin.title}")
    private String adminTitle;

    @Value("${admin.gender}")
    private String adminGender;

    @Value("${admin.password}")
    private String adminPassword;

    @Value("${admin.address}")
    private String adminAddress;

    @Value("${admin.city}")
    private String adminCity;

    @Value("${admin.state}")
    private String adminState;

    @Value("${admin.pinCode}")
    private String adminPinCode;

    @Value("${admin.mobileNumber}")
    private String adminMobileNumber;

    @Value("${admin.role}")
    private String adminRole;

    @Bean
    public CommandLineRunner createAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByEmail(adminEmail).isEmpty()) {
                User admin = new User();
                admin.setTitle(adminTitle);
                admin.setName(adminName);
                admin.setGender(adminGender);
                admin.setEmail(adminEmail);
                admin.setPassword(passwordEncoder.encode(adminPassword));
                admin.setAddress(adminAddress);
                admin.setCity(Long.parseLong(adminCity));
                admin.setState(Long.parseLong(adminState));
                admin.setPinCode(adminPinCode);
                admin.setMobileNumber(adminMobileNumber);
                admin.setVerified(true);
                admin.setRole(adminRole);
                admin.setOtp(null);
                admin.setOtpExpiry(LocalDateTime.now());
                userRepository.save(admin);
                System.out.println("Admin user created successfully.");
            }
        };
    }
}
