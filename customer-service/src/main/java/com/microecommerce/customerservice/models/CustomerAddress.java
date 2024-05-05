package com.microecommerce.customerservice.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerAddress {
    // TODO: Validate address format, e.g. no special characters
    // and address, city, postal code, country, and state from a list of valid values

    @Column(nullable = false, length = 1000)
    private String address;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String postalCode;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false, length = 1000)
    private String comment;
}
