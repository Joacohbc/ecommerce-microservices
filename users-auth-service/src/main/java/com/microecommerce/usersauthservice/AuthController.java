package com.microecommerce.usersauthservice;

import com.microecommerce.usersauthservice.service.AuthService;
import com.microecommerce.utilitymodule.models.users.UserCredentials;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    public AuthController(AuthService authService, AuthenticationManager authenticationManager) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
    }

    // TODO: Implement return JSON
    @PostMapping("/register")
    public String addUser(@RequestBody UserCredentials userCredentials) {
        return authService.saveUser(userCredentials);
    }

    // TODO: Implement return JSON
    @PostMapping("/login")
    public String getToken(@RequestBody UserCredentials userCredentials) {
        UsernamePasswordAuthenticationToken user = new UsernamePasswordAuthenticationToken(userCredentials.getUsername(), userCredentials.getPassword());
        Authentication auth = authenticationManager.authenticate(user);

        if(auth.isAuthenticated()) {
            return authService.generateToken(userCredentials);
        } else {
            return "Invalid credentials";
        }
    }

    // TODO: Implement return JSON
    @GetMapping("/validate")
    public String validateToken(@RequestParam("token") String token) {
        try {
            authService.validateToken(token);
            return "Token is valid";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

}
