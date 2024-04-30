package com.microecommerce.utilitymodule.models.users;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Entity
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

//    @Column(unique = true, nullable = false)
//    @Email
//    private String email;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(
//            name = "user_role",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "role_id"),
//            uniqueConstraints = { @UniqueConstraint(columnNames = {"user_id", "role_id"}) }
//    )
//    private List<Role> roles;
//
//    private boolean active;
}
