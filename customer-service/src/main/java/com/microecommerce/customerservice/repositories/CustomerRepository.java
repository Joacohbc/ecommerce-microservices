package com.microecommerce.customerservice.repositories;

import com.microecommerce.customerservice.models.Customer;
import com.microecommerce.utilitymodule.interfaces.CustomJPARepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends CustomJPARepository<Customer, Long> {
    Customer findByEmail(String email);
    Customer findByCustomerId(Long id);
    Customer findByCredentialsId(Long id);
    Customer findByEmailAndCredentialsId(String email, Long id);
}
