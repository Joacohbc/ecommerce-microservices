package com.microecommerce.customerservice.services;

import com.microecommerce.customerservice.models.Customer;
import com.microecommerce.customerservice.models.StoreOwner;
import com.microecommerce.customerservice.repositories.CustomerRepository;
import com.microecommerce.customerservice.repositories.StoreOwnerRepository;
import com.microecommerce.customerservice.services.interfaces.IStoreOwnerService;
import com.microecommerce.utilitymodule.exceptions.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StoreOwnerService implements IStoreOwnerService {

    private final StoreOwnerRepository storeOwnerRepository;
    private final CustomerRepository customerRepository;

    public StoreOwnerService(StoreOwnerRepository storeOwnerRepository, CustomerRepository customerRepository) {
        this.storeOwnerRepository = storeOwnerRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public StoreOwner createStoreOwner(StoreOwner storeOwner) {
        storeOwner.setIsActive(false);
        return storeOwnerRepository.save(storeOwner);
    }

    @Override
    public StoreOwner getStoreOwnerByEmail(String email) {
        return storeOwnerRepository.findByEmail(email);
    }

    @Override
    public StoreOwner getStoreOwnerById(Long customerId) {
        return storeOwnerRepository.findByCustomerId(customerId);
    }

    @Override
    public StoreOwner updateStoreOwner(Long customerId, StoreOwner storeOwner) throws EntityNotFoundException {
        StoreOwner existingStoreOwner = storeOwnerRepository
                .findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("StoreOwner not found"));

        existingStoreOwner.setFirstName(storeOwner.getFirstName());
        existingStoreOwner.setMiddleName(storeOwner.getMiddleName());
        existingStoreOwner.setLastName(storeOwner.getLastName());
        existingStoreOwner.setEmail(storeOwner.getEmail());
        existingStoreOwner.setPhone(storeOwner.getPhone());
        existingStoreOwner.setAddress(storeOwner.getAddress());
        existingStoreOwner.setIsActive(storeOwner.getIsActive());
        return storeOwnerRepository.save(existingStoreOwner);
    }

    @Override
    public void activateStoreOwner(Long customerId) {
        Optional<StoreOwner> buyer = storeOwnerRepository.findById(customerId);
        if (buyer.isPresent()) {
            buyer.get().setIsActive(true);
            storeOwnerRepository.save(buyer.get());
        }
    }

    @Override
    public void deactivateStoreOwner(Long customerId) {
        Optional<StoreOwner> buyer = storeOwnerRepository.findById(customerId);
        if (buyer.isPresent()) {
            buyer.get().setIsActive(false);
            storeOwnerRepository.save(buyer.get());
        }
    }

    @Override
    public StoreOwner createExistingCustomerAsStoreOwner(Long customerId, StoreOwner storeOwner) {
        Optional<Customer> existingCustomer = customerRepository.findById(customerId);
        if (existingCustomer.isPresent()) {
            StoreOwner newStoreOwner = new StoreOwner();
            newStoreOwner.setCustomerId(existingCustomer.get().getCustomerId());
            newStoreOwner.setCredentialsId(existingCustomer.get().getCredentialsId());
            newStoreOwner.setFirstName(existingCustomer.get().getFirstName());
            newStoreOwner.setMiddleName(existingCustomer.get().getMiddleName());
            newStoreOwner.setLastName(existingCustomer.get().getLastName());
            newStoreOwner.setEmail(existingCustomer.get().getEmail());
            newStoreOwner.setPhone(existingCustomer.get().getPhone());
            newStoreOwner.setAddress(existingCustomer.get().getAddress());

            // Specific fields of StoreOwner
            newStoreOwner.setIsActive(true);
            return storeOwnerRepository.save(storeOwner);
        }
        return null;
    }
}
