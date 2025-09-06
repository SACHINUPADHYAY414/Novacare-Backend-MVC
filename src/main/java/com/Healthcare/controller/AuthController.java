package com.Healthcare.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.Healthcare.dto.OtpVerificationDto;
import com.Healthcare.dto.UserLoginDto;
import com.Healthcare.dto.UserRegistrationDto;
import com.Healthcare.dto.UserResponseDto;
import com.Healthcare.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody UserRegistrationDto dto) {
        // Directly return the ResponseEntity from service
        return userService.registerUser(dto);
    }

    @PostMapping("/otp-verify")
    public ResponseEntity<Map<String, Object>> verifyOtp(@RequestBody OtpVerificationDto dto) {
        Map<String, Object> response = userService.verifyOtp(dto);

        if (response.containsKey("token")) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<Map<String, Object>> resendOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        return userService.resendOtp(email);
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDto dto) {
        Map<String, Object> response = userService.loginUser(dto);
        if ("OTP sent to your email for login verification".equals(response.get("message"))) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }

    @PostMapping("/login/otp-verify")
    public ResponseEntity<?> verifyLoginOtp(@RequestBody OtpVerificationDto dto) {
        try {
            Map<String, Object> result = userService.verifyLoginOtp(dto);

            if (result == null || result.get("user") == null || result.get("token") == null) {
                return ResponseEntity.badRequest().body(Map.of("message", "Invalid or expired OTP"));
            }

            UserResponseDto userDto = (UserResponseDto) result.get("user");
            String token = (String) result.get("token");

            Map<String, Object> userMap = new HashMap<>();
            userMap.put("id", userDto.getId() != null ? userDto.getId() : 0L);
            userMap.put("title", userDto.getTitle() != null ? userDto.getTitle() : "");
            userMap.put("name", userDto.getName() != null ? userDto.getName() : "");
            userMap.put("gender", userDto.getGender() != null ? userDto.getGender() : "");
            userMap.put("email", userDto.getEmail() != null ? userDto.getEmail() : "");
            userMap.put("address", userDto.getAddress() != null ? userDto.getAddress() : "");
            userMap.put("city", userDto.getCity() != null ? userDto.getCity() : "");
            userMap.put("state", userDto.getState() != null ? userDto.getState() : "");
            userMap.put("pinCode", userDto.getPinCode() != null ? userDto.getPinCode() : "");
            userMap.put("mobileNumber", userDto.getMobileNumber() != null ? userDto.getMobileNumber() : "");
            userMap.put("role", userDto.getRole() != null ? userDto.getRole() : "USER");

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("token", token != null ? token : "");
            response.put("user", userMap);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("message", "Internal server error"));
        }
    }
}


