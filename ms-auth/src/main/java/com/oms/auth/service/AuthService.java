package com.oms.auth.service;

import com.oms.auth.dto.AuthRequest;
import com.oms.auth.dto.AuthResponse;
import com.oms.auth.dto.RegisterRequest;
import com.oms.auth.model.Role;
import com.oms.auth.model.User;
import com.oms.auth.repository.RoleRepository;
import com.oms.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    
    // In-memory token store for MVP (would use Redis in production)
    private final Map<String, String> refreshTokenStore = new HashMap<>();

    @Autowired
    public AuthService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest request) {
        // Check if user exists
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
        
        // Get default user role
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new IllegalStateException("Default role not found"));

        // Create user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setName(request.getName());
        user.setSurname(request.getSurname());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(userRole);
        
        userRepository.save(user);
        
        return generateTokens(user);
    }

    public AuthResponse authenticate(AuthRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));
        
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid username or password");
        }
        
        return generateTokens(user);
    }

    public AuthResponse refreshToken(String refreshToken) {
        String username = refreshTokenStore.get(refreshToken);
        
        if (username == null) {
            throw new BadCredentialsException("Invalid refresh token");
        }
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("User not found"));
        
        // Remove old refresh token
        refreshTokenStore.remove(refreshToken);
        
        return generateTokens(user);
    }

    public void logout(String refreshToken) {
        refreshTokenStore.remove(refreshToken);
    }

    public boolean validateToken(String token) {
        return jwtService.validateToken(token);
    }

    private AuthResponse generateTokens(User user) {
        String accessToken = jwtService.generateToken(user);
        String refreshToken = UUID.randomUUID().toString();
        
        // Store refresh token
        refreshTokenStore.put(refreshToken, user.getUsername());
        
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .username(user.getUsername())
                .role(user.getRole().getName())
                .build();
    }
}
