package com.insurance.system.controller;

import com.insurance.system.dto.AuthResponse;
import com.insurance.system.dto.RegisterRequest;
import com.insurance.system.entity.Role;
import com.insurance.system.entity.User;
import com.insurance.system.repository.UserRepository;
import com.insurance.system.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${admin.registration.pin}")
    private String adminPin;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()) != null) {
            return ResponseEntity.badRequest().body("Email already exists.");
        }

        if (request.getRole() == Role.ADMIN) {
            if (request.getAdminPin() == null || !request.getAdminPin().equals(adminPin)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid security pin for admin registration.");
            }
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody com.insurance.system.model.AuthRequest request) {
        User user = userRepository.findByEmail(request.getEmail());

        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }

        if (user.isBlocked()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: You are blocked, contact the admin");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        return ResponseEntity.ok(new AuthResponse(token, user.getUserId(), user.getRole().name()));
    }
}
