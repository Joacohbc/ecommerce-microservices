package com.microecommerce.customerservice.services.interfaces;

import com.microecommerce.customerservice.models.Buyer;
import com.microecommerce.customerservice.models.StoreOwner;
import com.microecommerce.utilitymodule.exceptions.EntityNotFoundException;

public interface ICustomerService {
    Buyer createBuyer(Buyer buyer);
    Buyer getBuyerByEmail(String email);
    Buyer getBuyerById(Long customerId);
    Buyer updateBuyer(Long customerId, Buyer buyer) throws EntityNotFoundException;
    void deactivateBuyer(Long customerId);
    void activateBuyer(Long customerId);

    StoreOwner createStoreOwner(StoreOwner storeOwner);
    StoreOwner getStoreOwnerByEmail(String email);
    StoreOwner getStoreOwnerById(Long customerId);
    StoreOwner updateStoreOwner(Long id, StoreOwner storeOwner);
    void deactivateStoreOwner(Long customerId);
    void activateStoreOwner(Long customerId);

    Buyer createExistingCustomerAsBuyer(Long customerId, Buyer buyerInfo);
    StoreOwner createExistingCustomerAsStoreOwner(Long customerId, StoreOwner storeOwnerInfo);
}
