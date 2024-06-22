package com.microecommerce.customerservice.models;

import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreOwner extends Customer {
    // private List<StoreDTO> stores;
}