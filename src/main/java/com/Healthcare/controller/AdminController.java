package com.healthcare.controller;

import com.healthcare.dto.AppointmentDetailsDto;
import com.healthcare.dto.UserRegistrationDto;
import com.healthcare.dto.UserResponseDto;
import com.healthcare.model.User;
import com.healthcare.repository.CityRepository;
import com.healthcare.repository.StateRepository;
import com.healthcare.repository.UserRepository;
import com.healthcare.service.BookAppointmentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private BookAppointmentService appointmentService;

    // ================= GET ALL USERS =================
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAllByOrderByNameAsc());
    }

    // ================= VERIFY USER =================
    @PostMapping("/verify-user")
    public ResponseEntity<?> verifyUser(@RequestParam String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setVerified(true);
            userRepository.save(user);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "User verified successfully");
            response.put("user", new UserResponseDto(
                    user.getId(),
                    user.getTitle(),
                    user.getName(),
                    user.getGender(),
                    user.getEmail(),
                    user.getAddress(),
                    user.getCity(),
                    user.getState(),
                    user.getPinCode(),
                    user.getMobileNumber(),
                    user.getRole(),
                    user.getUhid()
            ));
            response.put("otpSkipped", true);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(Map.of("message", "User not found"));
    }

    // ================= ADD USER =================
    @PostMapping("/user")
    public ResponseEntity<?> addUser(@RequestBody UserRegistrationDto dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            return ResponseEntity.status(409).body(Map.of("message", "User with this email already exists"));
        }
        if (dto.getCity() != null && cityRepository.findById(dto.getCity()).isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid city ID"));
        }
        if (dto.getState() != null && stateRepository.findById(dto.getState()).isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid state ID"));
        }

        User user = new User();
        user.setTitle(dto.getTitle());
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setAddress(dto.getAddress());
        user.setCity(dto.getCity() != null ? dto.getCity().longValue() : null);
        user.setState(dto.getState() != null ? dto.getState().longValue() : null);
        user.setPinCode(dto.getPinCode());
        user.setMobileNumber(dto.getMobileNumber());
        user.setGender(dto.getGender());
        user.setRole(dto.getRole() != null ? dto.getRole() : "USER");
        user.setVerified(true);
        user.setOtp(null);
        user.setOtpExpiry(LocalDateTime.now());

        userRepository.save(user);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "User created successfully");
        response.put("user", new UserResponseDto(
                user.getId(),
                user.getTitle(),
                user.getName(),
                user.getGender(),
                user.getEmail(),
                user.getAddress(),
                user.getCity(),
                user.getState(),
                user.getPinCode(),
                user.getMobileNumber(),
                user.getRole(),
                user.getUhid()
        ));
        response.put("otpSkipped", true);

        return ResponseEntity.ok(response);
    }

    // ================= GET USER BY ID =================
    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("message", "User not found"));
        }

        User user = optionalUser.get();
        Map<String, Object> response = new HashMap<>();
        response.put("user", new UserResponseDto(
                user.getId(),
                user.getTitle(),
                user.getName(),
                user.getGender(),
                user.getEmail(),
                user.getAddress(),
                user.getCity(),
                user.getState(),
                user.getPinCode(),
                user.getMobileNumber(),
                user.getRole(),
                user.getUhid()
        ));
        response.put("otpSkipped", true);

        return ResponseEntity.ok(response);
    }

    // ================= UPDATE USER =================
    @PutMapping("/user/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserRegistrationDto dto) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("message", "User not found"));
        }
        User user = optionalUser.get();
        user.setTitle(dto.getTitle());
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setAddress(dto.getAddress());
        user.setCity(dto.getCity() != null ? dto.getCity().longValue() : null);
        user.setState(dto.getState() != null ? dto.getState().longValue() : null);
        user.setPinCode(dto.getPinCode());
        user.setMobileNumber(dto.getMobileNumber());
        user.setGender(dto.getGender());
        user.setRole(dto.getRole());
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        userRepository.save(user);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "User updated successfully");
        response.put("user", new UserResponseDto(
                user.getId(),
                user.getTitle(),
                user.getName(),
                user.getGender(),
                user.getEmail(),
                user.getAddress(),
                user.getCity(),
                user.getState(),
                user.getPinCode(),
                user.getMobileNumber(),
                user.getRole(),
                user.getUhid()
        ));
        response.put("otpSkipped", true);

        return ResponseEntity.ok(response);
    }

    // ================= DELETE USER =================
    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.status(404).body(Map.of("message", "User not found"));
        }
        userRepository.deleteById(id);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "User deleted successfully");
        response.put("otpSkipped", true);
        return ResponseEntity.ok(response);
    }

    // ================= GET ALL APPOINTMENTS =================
    @GetMapping("/appointments-details")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AppointmentDetailsDto>> getAllAppointmentDetails() {
        List<AppointmentDetailsDto> appointments = appointmentService.getAllAppointments();

        if (appointments.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(appointments);
    }
}
