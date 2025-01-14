package com.microecommerce.customerservice.repositories;

import com.microecommerce.customerservice.models.Buyer;
import com.microecommerce.utilitymodule.interfaces.CustomJPARepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuyerRepository extends CustomJPARepository<Buyer, Long> {
    Buyer findByEmail(String email);
    Buyer findByCustomerId(Long id);
    Buyer findByCredentialsId(Long id);
    Buyer findByEmailAndCredentialsId(String email, Long id);
    Buyer findByEmailAndIsActive(String email, Boolean isActive);
    Buyer findByCredentialsIdAndIsActive(Long id, Boolean isActive);
}
