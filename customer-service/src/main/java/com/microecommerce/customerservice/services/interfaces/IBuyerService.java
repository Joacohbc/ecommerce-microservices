package com.microecommerce.customerservice.services.interfaces;

import com.microecommerce.customerservice.models.Buyer;
import com.microecommerce.customerservice.models.StoreOwner;
import com.microecommerce.utilitymodule.exceptions.EntityNotFoundException;
import com.microecommerce.utilitymodule.exceptions.InvalidActionException;

public interface IBuyerService {
    Buyer createBuyer(Buyer buyer);
    Buyer getBuyerByEmail(String email) throws EntityNotFoundException;
    Buyer getBuyerById(Long customerId) throws EntityNotFoundException;
    Buyer updateBuyer(Long customerId, Buyer buyer) throws EntityNotFoundException;

    void activateBuyer(Long customerId) throws EntityNotFoundException, InvalidActionException;
    void deactivateBuyer(Long customerId) throws EntityNotFoundException, InvalidActionException;
}
