package com.microecommerce.utilitymodule.models.users;

import com.microecommerce.utilitymodule.exceptions.InvalidEntityException;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "users_credentials")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCredentials implements Serializable  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Lob
    @Column(nullable = false)
    private String password;

    public static void validateCredentials(UserCredentials userCredentials) throws InvalidEntityException {
        // Validate username and password
        if (userCredentials.getUsername() == null || userCredentials.getUsername().isEmpty()) {
            throw new InvalidEntityException("Username is required");
        }

        if (userCredentials.getPassword() == null || userCredentials.getPassword().isEmpty()) {
            throw new InvalidEntityException("Password is required");
        }

        if(userCredentials.getUsername().length() < 4 || userCredentials.getUsername().length() > 50) {
            throw new InvalidEntityException("Username must be between 4 and 50 characters long");
        }

        if(userCredentials.getPassword().length() < 8 || userCredentials.getPassword().length() > 50) {
            throw new InvalidEntityException("Password must be between 8 and 50 characters long");
        }

        if(!userCredentials.getUsername().matches("^[a-zA-Z0-9]*$")) {
            throw new InvalidEntityException("Username must contain only letters and numbers");
        }

        if(!userCredentials.getPassword().matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$")) {
            throw new InvalidEntityException("Password must contain at least one uppercase letter (A-Z), one lowercase letter (a-z), one number (1-9), and one special character (#?!@$%^&*-])");
        }
    }
}
