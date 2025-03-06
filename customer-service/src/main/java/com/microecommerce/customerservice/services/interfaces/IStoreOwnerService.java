package com.microecommerce.customerservice.services.interfaces;

import com.microecommerce.customerservice.models.Buyer;
import com.microecommerce.customerservice.models.StoreOwner;
import com.microecommerce.utilitymodule.exceptions.EntityNotFoundException;

public interface IStoreOwnerService {
    StoreOwner createStoreOwner(StoreOwner storeOwner);
    StoreOwner getStoreOwnerByEmail(String email);
    StoreOwner getStoreOwnerById(Long customerId);
    StoreOwner updateStoreOwner(Long id, StoreOwner storeOwner) throws EntityNotFoundException;

    void deactivateStoreOwner(Long customerId);
    void activateStoreOwner(Long customerId);

    StoreOwner createExistingCustomerAsStoreOwner(Long customerId, StoreOwner storeOwnerInfo);
}
