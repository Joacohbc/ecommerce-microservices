package com.microecommerce.customerservice.services.interfaces;

import com.microecommerce.customerservice.models.StoreOwner;
import com.microecommerce.utilitymodule.exceptions.EntityNotFoundException;
import com.microecommerce.utilitymodule.exceptions.InvalidActionException;

import java.util.Optional;

public interface IStoreOwnerService {
    StoreOwner createStoreOwner(StoreOwner storeOwner);
    StoreOwner getStoreOwnerByEmail(String email) throws EntityNotFoundException;
    StoreOwner getStoreOwnerById(Long customerId) throws EntityNotFoundException;
    StoreOwner updateStoreOwner(Long id, StoreOwner storeOwner) throws EntityNotFoundException;

    void deactivateStoreOwner(Long customerId) throws EntityNotFoundException, InvalidActionException;
    void activateStoreOwner(Long customerId) throws EntityNotFoundException, InvalidActionException;

    StoreOwner createExistingCustomerAsStoreOwner(Long customerId, StoreOwner storeOwnerInfo) throws EntityNotFoundException;
}
