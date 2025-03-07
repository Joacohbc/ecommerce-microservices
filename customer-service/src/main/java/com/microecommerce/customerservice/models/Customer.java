package com.microecommerce.customerservice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class Customer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

    // This file is mandatory for the Customer Service to work
    // It is not unique because the same credentials can be used by multiple customers (same person as Buyers and StoreOwners, or even multiple StoreOwners
    // for the same store)
    @Column(nullable = false)
    private Long credentialsId;

    @Column(nullable = false)
    private String firstName;

    private String middleName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @ElementCollection(fetch = FetchType.LAZY)
    private List<Phone> phone;

    @ElementCollection(fetch = FetchType.LAZY)
    private List<Address> address;
}
