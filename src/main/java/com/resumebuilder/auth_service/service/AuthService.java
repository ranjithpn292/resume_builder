package com.resumebuilder.auth_service.service;

import com.resumebuilder.auth_service.config.JwtService;
import com.resumebuilder.auth_service.dto.AuthResponse;
import com.resumebuilder.auth_service.dto.LoginRequest;
import com.resumebuilder.auth_service.dto.RegisterRequest;
import com.resumebuilder.auth_service.entity.User;
import com.resumebuilder.auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public String register(RegisterRequest req) {
        // Optional: check if user already exists
        if (userRepository.findByEmail(req.getEmail()).isPresent()) {
            // throw new RuntimeException("User already exists with email: " + req.getEmail());
            return "User already exists with email: " + req.getEmail();
        }

        User user = User.builder()
                .fullName(req.getFullName())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .build();

        userRepository.save(user);
        // String token = jwtService.generateToken(user);
        // return new AuthResponse(token);
        return "user registered successfully";
    }

    public AuthResponse login(LoginRequest req) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
            );
        } catch (Exception e) {
            throw new UsernameNotFoundException("Invalid email or password");
        }

        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }
}
