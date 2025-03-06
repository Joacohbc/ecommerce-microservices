package com.microecommerce.customerservice.services.interfaces;

import com.microecommerce.customerservice.models.Buyer;
import com.microecommerce.customerservice.models.StoreOwner;
import com.microecommerce.utilitymodule.exceptions.EntityNotFoundException;

public interface IBuyerService {
    Buyer createBuyer(Buyer buyer);
    Buyer getBuyerByEmail(String email);
    Buyer getBuyerById(Long customerId);
    Buyer updateBuyer(Long customerId, Buyer buyer) throws EntityNotFoundException;

    void activateBuyer(Long customerId);
    void deactivateBuyer(Long customerId);

    Buyer createExistingCustomerAsBuyer(Long customerId, Buyer buyerInfo);
}
