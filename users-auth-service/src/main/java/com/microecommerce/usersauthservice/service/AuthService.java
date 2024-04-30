package com.microecommerce.usersauthservice.service;

import com.microecommerce.usersauthservice.repository.UserCredentialsRepository;
import com.microecommerce.utilitymodule.models.users.UserCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserCredentialsRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserCredentialsRepository userCredentialsRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.repository = userCredentialsRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public String saveUser(UserCredentials userCredentials) {
        userCredentials.setPassword(passwordEncoder.encode(userCredentials.getPassword()));
        repository.save(userCredentials);
        return "User saved successfully";
    }

    public String generateToken(UserCredentials userCredentials) {
        // TODO: Implement better exception handling
        UserCredentials user = repository.findByUsername(userCredentials.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));

        if (passwordEncoder.matches(userCredentials.getPassword(), user.getPassword())) {
            return jwtService.generateToken(user.getUsername());
        }

        return "Invalid credentials";
    }

    // TODO: Implement better exception handling
    public void validateToken(String token) throws Exception {
        try {
            jwtService.validateToken(token);
        } catch (Exception e) {
            throw new Exception("Invalid token");
        }
    }
}
