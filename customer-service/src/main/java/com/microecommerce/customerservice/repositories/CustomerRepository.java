package com.microecommerce.customerservice.repositories;

import com.microecommerce.customerservice.models.Customer;
import com.microecommerce.utilitymodule.interfaces.CustomJPARepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

// TODO: only testing propose (to avoid create a service and controller layer)
@RestResource
@Repository
public interface CustomerRepository extends CustomJPARepository<Customer, Long> {
}
