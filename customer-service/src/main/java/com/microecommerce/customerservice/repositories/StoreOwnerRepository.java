package com.microecommerce.customerservice.repositories;

import com.microecommerce.customerservice.models.StoreOwner;
import com.microecommerce.utilitymodule.interfaces.CustomJPARepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreOwnerRepository extends CustomJPARepository<StoreOwner, Long> {
    StoreOwner findByEmail(String email);
    StoreOwner findByCustomerId(Long id);
    StoreOwner findByCredentialsId(Long id);

    StoreOwner findByEmailAndCredentialsId(String email, Long id);
    StoreOwner findByEmailAndIsActive(String email, Boolean isActive);
    StoreOwner findByCredentialsIdAndIsActive(Long id, Boolean isActive);
}
