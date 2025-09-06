package com.Healthcare.controller;

import com.Healthcare.model.User;
import com.Healthcare.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @PostMapping("/verify-user")
    public ResponseEntity<String> verifyUser(@RequestParam String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setVerified(true);
            userRepository.save(user);
            return ResponseEntity.ok("User verified successfully.");
        }
        return ResponseEntity.badRequest().body("User not found.");
    }
}
