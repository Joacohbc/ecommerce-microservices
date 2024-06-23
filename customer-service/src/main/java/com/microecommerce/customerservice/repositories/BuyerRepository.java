package com.microecommerce.customerservice.repositories;

import com.microecommerce.customerservice.models.Buyer;
import com.microecommerce.customerservice.models.Customer;
import com.microecommerce.utilitymodule.interfaces.CustomJPARepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

@RestResource(path = "buyers") // TODO: only testing propose (to avoid create a service and controller layer)
@Repository
public interface BuyerRepository extends CustomJPARepository<Buyer, Long> {
}
