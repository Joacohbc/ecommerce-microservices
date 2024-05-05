package com.microecommerce.customerservice.models;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Customer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

    @Column(nullable = false, unique = true)
    private Long credencialsId;

    @Column(nullable = false)
    private String firstName;
    private String middleName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @ElementCollection(fetch = FetchType.LAZY)
    private List<CustomerPhone> phone;

    @ElementCollection(fetch = FetchType.LAZY)
    private List<CustomerAddress> address;

    @Column(nullable = false)
    private String city;
}
