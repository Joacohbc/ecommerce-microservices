package com.microecommerce.customerservice.models;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
public class Buyer extends Customer {
    @ElementCollection(fetch = FetchType.LAZY)
    private List<Address> address;

}
