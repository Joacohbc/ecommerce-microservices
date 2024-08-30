package com.microecommerce.usersauthservice.service;

import com.microecommerce.usersauthservice.exceptions.AuthenticationException;
import com.microecommerce.usersauthservice.repository.UserCredentialsRepository;
import com.microecommerce.utilitymodule.exceptions.EntityNotFoundException;
import com.microecommerce.utilitymodule.exceptions.InvalidEntityException;
import com.microecommerce.utilitymodule.models.users.CUserCredentials;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public CUserCredentials getUser(Long id) throws EntityNotFoundException {
        return repository.findById(id).
                map(u -> {
                    u.setPassword("");
                    return u;
                })
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public String saveUser(CUserCredentials credentials) throws InvalidEntityException {
        if (repository.findByUsernameIgnoreCase(credentials.getUsername()).isPresent()) {
            throw new InvalidEntityException("A user with that username already exists");
        }

        CUserCredentials.validateCredentials(credentials);
        credentials.setPassword(passwordEncoder.encode(credentials.getPassword()));

        repository.save(credentials);
        return "User saved successfully";
    }

    public String generateToken(CUserCredentials CUserCredentials) throws AuthenticationException {
        CUserCredentials user = repository.findByUsernameIgnoreCase(CUserCredentials.getUsername())
                .orElseThrow(() -> new AuthenticationException("User not found"));

        if (passwordEncoder.matches(CUserCredentials.getPassword(), user.getPassword())) {
            return jwtService.generateToken(user.getUsername());
        }

        throw new AuthenticationException("Invalid credentials");
    }

    public void validateToken(String token) throws AuthenticationException {
        try {
            jwtService.validateToken(token);
        } catch (Exception e) {
            throw new AuthenticationException("Invalid token");
        }
    }
}
