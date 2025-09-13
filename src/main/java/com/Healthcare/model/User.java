package com.healthcare.model;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String uhid;

    private String title;
    private String name;
    private String email;
    private String password;
    private String address;
    private String pinCode;
    private String mobileNumber;

    @Column(name = "is_verified")
    private boolean isVerified;

    private String otp;
    private LocalDateTime otpExpiry;

    @Column(name = "role")
    @Builder.Default
    private String role = "USER";

    private String gender;
    private Long city;
    private Long state;
}
