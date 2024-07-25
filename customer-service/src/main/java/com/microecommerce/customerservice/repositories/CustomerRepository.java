package com.microecommerce.customerservice.repositories;

import com.microecommerce.customerservice.models.Customer;
import com.microecommerce.utilitymodule.interfaces.CustomJPARepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends CustomJPARepository<Customer, Long> {
}
