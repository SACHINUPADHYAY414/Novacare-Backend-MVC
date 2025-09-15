package com.healthcare.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.healthcare.dto.OtpVerificationDto;
import com.healthcare.dto.UserLoginDto;
import com.healthcare.dto.UserRegistrationDto;
import com.healthcare.dto.UserResponseDto;
import com.healthcare.service.JwtService;
import com.healthcare.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;
    
    // ================= REGISTER NEW ACCOUNT =================
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody UserRegistrationDto dto) {
        ResponseEntity<Map<String, Object>> responseEntity = userService.registerUser(dto);
        Map<String, Object> body = new HashMap<>(responseEntity.getBody());
        body.put("otpSkipped", userService.isSkipOtp());
        return ResponseEntity.status(responseEntity.getStatusCode()).body(body);
    }

    // ================= VERIFY REGISTER OTP =================
    @PostMapping("/otp-verify")
    public ResponseEntity<Map<String, Object>> verifyOtp(@RequestBody OtpVerificationDto dto) {
        Map<String, Object> response = userService.verifyOtp(dto);
        if (response.containsKey("token")) {
            response.put("otpSkipped", userService.isSkipOtp());
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }

    // ================= RESEND OTP =================
    @PostMapping("/resend-otp")
    public ResponseEntity<Map<String, Object>> resendOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        ResponseEntity<Map<String, Object>> responseEntity = userService.resendOtp(email);
        Map<String, Object> body = new HashMap<>(responseEntity.getBody());
        body.put("otpSkipped", userService.isSkipOtp());
        return ResponseEntity.status(responseEntity.getStatusCode()).body(body);
    }

    // ================= LOGIN =================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDto dto) {
        Map<String, Object> response = userService.loginUser(dto);
        response.put("otpSkipped", userService.isSkipOtp());

        String message = (String) response.get("message");

        // ✅ Check for success messages more flexibly
        if (message != null && (
                message.contains("Login successful") ||
                message.contains("OTP sent")
            )) {
            return ResponseEntity.ok(response);
        }

        // ❌ Invalid credentials or other errors
        return ResponseEntity.badRequest().body(response);
    }

    
    // ================= VERIFY LOGIN OTP =================
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
            response.put("otpSkipped", userService.isSkipOtp());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("message", "Internal server error"));
        }
    }

    // ================= GET ALL USERS FOR ADMINS ONLY =================
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body(Map.of("message", "Authorization header missing or invalid"));
            }
            String token = authHeader.substring(7); // Remove "Bearer "

            if (!jwtService.isTokenValid(token, jwtService.extractUsername(token))) {
                return ResponseEntity.status(401).body(Map.of("message", "Invalid or expired token"));
            }

            List<String> roles = jwtService.extractRoles(token);
            if (roles == null || !roles.contains("ADMIN")) {
                return ResponseEntity.status(403).body(Map.of("message", "Access denied. Admins only."));
            }

            List<UserResponseDto> users = userService.getAllUsers();
            return ResponseEntity.ok(users);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("message", "Failed to fetch users"));
        }
    }

    // ================= RESET PASSWORD =================

    // Step 1: Request reset OTP
    @PostMapping("/reset-password/request")
    public ResponseEntity<Map<String, Object>> requestResetPassword(@RequestBody Map<String, String> request) {
        ResponseEntity<Map<String, Object>> responseEntity = userService.initiatePasswordReset(request.get("email"));
        Map<String, Object> body = new HashMap<>(responseEntity.getBody());
        body.put("otpSkipped", userService.isSkipOtp());
        return ResponseEntity.status(responseEntity.getStatusCode()).body(body);
    }

    // Step 2: Verify reset OTP
    @PostMapping("/reset-password/verify-otp")
    public ResponseEntity<Map<String, Object>> verifyResetOtp(@RequestBody Map<String, String> request) {
        ResponseEntity<Map<String, Object>> responseEntity = userService.verifyResetOtp(request.get("email"), request.get("otp"));
        Map<String, Object> body = new HashMap<>(responseEntity.getBody());
        body.put("otpSkipped", userService.isSkipOtp());
        return ResponseEntity.status(responseEntity.getStatusCode()).body(body);
    }

    // Step 3: Confirm new password
    @PostMapping("/reset-password/confirm")
    public ResponseEntity<Map<String, Object>> confirmResetPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String newPassword = request.get("newPassword");
        ResponseEntity<Map<String, Object>> responseEntity = userService.resetPassword(email, newPassword);
        Map<String, Object> body = new HashMap<>(responseEntity.getBody());
        body.put("otpSkipped", userService.isSkipOtp());
        return ResponseEntity.status(responseEntity.getStatusCode()).body(body);
    }
}
