package com.Healthcare.service;

import java.time.LocalDateTime;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.Healthcare.dto.UserLoginDto;
import com.Healthcare.dto.UserRegistrationDto;
import com.Healthcare.dto.UserResponseDto;
import com.Healthcare.dto.OtpVerificationDto;
import com.Healthcare.model.City;
import com.Healthcare.model.State;
import com.Healthcare.model.User;
import com.Healthcare.repository.CityRepository;
import com.Healthcare.repository.StateRepository;
import com.Healthcare.repository.UserRepository;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private StateRepository stateRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // ================== REGISTER ==================
    public ResponseEntity<Map<String, Object>> registerUser(UserRegistrationDto dto) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Check if email already exists
            if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
                response.put("message", "User already exists");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }

            // Validate city
            Optional<City> cityOpt = cityRepository.findById(dto.getCity());
            if (cityOpt.isEmpty()) {
                response.put("message", "Invalid city ID");
                return ResponseEntity.badRequest().body(response);
            }

            // Validate state
            Optional<State> stateOpt = stateRepository.findById(dto.getState());
            if (stateOpt.isEmpty()) {
                response.put("message", "Invalid state ID");
                return ResponseEntity.badRequest().body(response);
            }

            // Create User entity
            User user = new User();
            user.setTitle(dto.getTitle());
            user.setName(dto.getName());
            user.setEmail(dto.getEmail());
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
            user.setAddress(dto.getAddress());
            user.setCity((long) cityOpt.get().getCityId());
            user.setState((long) stateOpt.get().getStateId());
            user.setPinCode(dto.getPinCode());
            user.setMobileNumber(dto.getMobileNumber());
            user.setGender(dto.getGender());
            user.setVerified(false);
            user.setRole(dto.getRole() != null && dto.getRole().equalsIgnoreCase("ADMIN") ? "ADMIN" : "USER");

            // Generate OTP
            String otp = generateOtp();
            user.setOtp(otp);
            user.setOtpExpiry(LocalDateTime.now().plusMinutes(5));

            userRepository.save(user);

            // Send OTP email
            emailService.sendOtpEmail(user.getEmail(), otp);

            response.put("message", "OTP sent to your email");
            response.put("email", user.getEmail());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "Registration failed due to server error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // ================== VERIFY REGISTRATION OTP ==================
    public Map<String, Object> verifyOtp(OtpVerificationDto dto) {
        Map<String, Object> response = new HashMap<>();
        Optional<User> userOpt = userRepository.findByEmailAndOtp(dto.getEmail(), dto.getOtp());

        if (userOpt.isEmpty()) {
            response.put("message", "Invalid OTP or email");
            return response;
        }

        User user = userOpt.get();

        if (user.getOtpExpiry() == null || user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            response.put("message", "OTP expired");
            return response;
        }

        // ✅ Mark user as verified
        user.setVerified(true);
        user.setOtp(null);
        user.setOtpExpiry(null);
        userRepository.save(user);

        UserResponseDto userDto = new UserResponseDto(
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
                user.getRole()
        );

        // Prepare roles for token
        List<String> roles = new ArrayList<>();
        if ("ADMIN".equalsIgnoreCase(user.getRole())) {
            roles.add("ADMIN");
        } else {
            roles.add("USER");
        }

        String token = jwtService.generateToken(user.getEmail(), roles);

        response.put("user", userDto);
        response.put("token", token);
        return response;
    }

    // ================== LOGIN ==================
    public Map<String, Object> loginUser(UserLoginDto dto) {
        Map<String, Object> response = new HashMap<>();
        Optional<User> userOpt = userRepository.findByEmail(dto.getEmail());

        if (userOpt.isEmpty()) {
            response.put("message", "User not found");
            return response;
        }

        User user = userOpt.get();
        if (!user.isVerified()) {
            response.put("message", "User not verified");
            return response;
        }

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            response.put("message", "Invalid credentials");
            return response;
        }

        // Generate OTP for login
        String otp = generateOtp();
        user.setOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(5));
        userRepository.save(user);

        emailService.sendOtpEmail(user.getEmail(), otp);

        response.put("message", "OTP sent to your email for login verification");
        response.put("email", user.getEmail());
        return response;
    }

    // ================== VERIFY LOGIN OTP ==================
    public Map<String, Object> verifyLoginOtp(OtpVerificationDto dto) {
        Map<String, Object> response = new HashMap<>();
        Optional<User> userOpt = userRepository.findByEmailAndOtp(dto.getEmail(), dto.getOtp());

        if (userOpt.isEmpty()) {
            response.put("message", "Invalid OTP or email");
            return response;
        }

        User user = userOpt.get();

        if (user.getOtpExpiry() == null || user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            response.put("message", "OTP expired");
            return response;
        }

        user.setOtp(null);
        user.setOtpExpiry(null);
        userRepository.save(user);

        UserResponseDto userDto = new UserResponseDto(
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
                user.getRole()
        );

        // Roles for JWT
        List<String> roles = new ArrayList<>();
        if ("ADMIN".equalsIgnoreCase(user.getRole())) {
            roles.add("ADMIN");
        } else {
            roles.add("USER");
        }

        String token = jwtService.generateToken(user.getEmail(), roles);

        response.put("user", userDto);
        response.put("token", token);
        return response;
    }

    // ================== RESEND OTP ==================
    public ResponseEntity<Map<String, Object>> resendOtp(String email) {
        Map<String, Object> response = new HashMap<>();
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            response.put("message", "User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        User user = userOpt.get();

        String otp = generateOtp();
        user.setOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(5));
        userRepository.save(user);

        try {
            emailService.sendOtpEmail(email, otp);
        } catch (Exception e) {
            response.put("message", "Failed to send OTP email");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        response.put("message", user.isVerified()
                ? "OTP resent for login"
                : "OTP resent for registration");
        response.put("email", email);

        return ResponseEntity.ok(response);
    }

    // ================== GENERATE OTP ==================
    private String generateOtp() {
        int otp = 100000 + new Random().nextInt(900000);
        return String.valueOf(otp);
    }
    
    // ================== ALL USERS FOR ADMIN ACCESS ==================
    public List<UserResponseDto> getAllUsers() {
    	  List<User> users = userRepository.findAllByOrderByNameAsc(); 
        List<UserResponseDto> userDtos = new ArrayList<>();
        for (User user : users) {
            userDtos.add(new UserResponseDto(
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
                    user.getRole()
            ));
        }
        return userDtos;
    }

}
