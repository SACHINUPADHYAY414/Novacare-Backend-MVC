package com.Healthcare.controller;

import com.Healthcare.dto.UserRegistrationDto;
import com.Healthcare.model.User;
import com.Healthcare.repository.CityRepository;
import com.Healthcare.repository.StateRepository;
import com.Healthcare.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAllByOrderByNameAsc());
    }

    @PostMapping("/verify-user")
    public ResponseEntity<?> verifyUser(@RequestParam String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setVerified(true);
            userRepository.save(user);
            return ResponseEntity.ok(Map.of("message", "User verified successfully"));
        }
        return ResponseEntity.badRequest().body(Map.of("message", "User not found"));
    }

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
        return ResponseEntity.ok(Map.of("message", "User created successfully"));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("message", "User not found"));
        }
        return ResponseEntity.ok(optionalUser.get());
    }

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
        return ResponseEntity.ok(Map.of("message", "User updated successfully"));
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.status(404).body(Map.of("message", "User not found"));
        }
        userRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
    }
}
