package com.microecommerce.usersauthservice.controllers;

import com.microecommerce.usersauthservice.exceptions.AuthenticationException;
import com.microecommerce.usersauthservice.service.AuthService;
import com.microecommerce.utilitymodule.exceptions.EntityNotFoundException;
import com.microecommerce.utilitymodule.exceptions.InvalidEntityException;
import com.microecommerce.utilitymodule.exceptions.RestExceptionHandler;
import com.microecommerce.utilitymodule.models.users.UserCredentials;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    public AuthController(AuthService authService, AuthenticationManager authenticationManager) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUser(@PathVariable Long id) {
        try {
            Map<String, Object> response = RestExceptionHandler.createJsonResponse(
                    "User found successfully",
                    authService.getUser(id),
                    HttpStatus.OK);

            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            Map<String, Object> response = RestExceptionHandler.createJsonResponse(e.getMessage(), null, HttpStatus.NOT_FOUND);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Object> addUser(@RequestBody UserCredentials userCredentials) {
        try {
            Map<String, Object> response = RestExceptionHandler.createJsonResponse(
                    authService.saveUser(userCredentials),
                    null,
                    HttpStatus.OK);
            return ResponseEntity.ok(response);
        } catch (InvalidEntityException e) {
            Map<String, Object> response = RestExceptionHandler.createJsonResponse(e.getMessage(), null, HttpStatus.BAD_REQUEST);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> getToken(@RequestBody UserCredentials userCredentials) {
        try {
            UsernamePasswordAuthenticationToken user = new UsernamePasswordAuthenticationToken(userCredentials.getUsername(), userCredentials.getPassword());
            Authentication auth = authenticationManager.authenticate(user);

            if(auth.isAuthenticated()) {
                String token = authService.generateToken(userCredentials);
                Map<String, Object> response = RestExceptionHandler.createJsonResponse("Token generated successfully", token, HttpStatus.OK);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(RestExceptionHandler.createJsonResponse("Invalid credentials",null, HttpStatus.UNAUTHORIZED));
            }
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(RestExceptionHandler.createJsonResponse("Invalid credentials", null, HttpStatus.UNAUTHORIZED));
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<Object> validateToken(@RequestParam("token") String token) {
        try {
            authService.validateToken(token);
            return ResponseEntity.ok(RestExceptionHandler.createJsonResponse("Token is valid", null, HttpStatus.OK));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(RestExceptionHandler.createJsonResponse("Invalid token", null, HttpStatus.UNAUTHORIZED));
        }
    }
}
