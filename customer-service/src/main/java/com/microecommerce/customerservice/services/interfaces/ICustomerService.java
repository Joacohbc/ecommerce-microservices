package com.microecommerce.customerservice.services.interfaces;

import com.microecommerce.customerservice.models.Buyer;
import com.microecommerce.customerservice.models.StoreOwner;

public interface ICustomerService {
    Buyer createBuyer(Buyer buyer);
    Buyer getBuyerByEmail(String email);
    Buyer getBuyerById(Long customerId);
    Buyer updateBuyer(Long customerId, Buyer buyer);
    void deleteBuyer(Long customerId);

    StoreOwner createStoreOwner(StoreOwner storeOwner);
    StoreOwner getStoreOwnerByEmail(String email);
    StoreOwner getStoreOwnerById(Long customerId);
    StoreOwner updateStoreOwner(Long id, StoreOwner storeOwner);
    void deleteStoreOwner(Long customerId);

    void activateCustomer(Long customerId);
    void deactivateCustomer(Long customerId);

    Buyer createExistingCustomerAsBuyer(Long customerId, Buyer buyerInfo);
    StoreOwner createExistingCustomerAsStoreOwner(Long customerId, StoreOwner storeOwnerInfo);
}
