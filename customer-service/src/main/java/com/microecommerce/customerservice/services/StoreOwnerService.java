package com.microecommerce.customerservice.services;

import com.microecommerce.customerservice.models.Customer;
import com.microecommerce.customerservice.models.StoreOwner;
import com.microecommerce.customerservice.repositories.CustomerRepository;
import com.microecommerce.customerservice.repositories.StoreOwnerRepository;
import com.microecommerce.customerservice.services.interfaces.IStoreOwnerService;
import com.microecommerce.utilitymodule.exceptions.EntityNotFoundException;
import com.microecommerce.utilitymodule.exceptions.InvalidActionException;
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
    public StoreOwner getStoreOwnerByEmail(String email) throws EntityNotFoundException {
        return Optional.of(storeOwnerRepository.findByEmail(email))
                .orElseThrow(() -> new EntityNotFoundException("StoreOwner with Email " + email + " not found"));
    }

    @Override
    public StoreOwner getStoreOwnerById(Long customerId) throws EntityNotFoundException {
        return storeOwnerRepository
                .findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("StoreOwner with the Id " + customerId + " not found"));
    }

    @Override
    public StoreOwner createStoreOwner(StoreOwner storeOwner) {
        storeOwner.setIsActive(false);

        // TODO: Add Validations
        return storeOwnerRepository.save(storeOwner);
    }

    @Override
    public StoreOwner updateStoreOwner(Long customerId, StoreOwner storeOwner) throws EntityNotFoundException {
        StoreOwner existingStoreOwner = getStoreOwnerById(customerId);
        existingStoreOwner.setFirstName(storeOwner.getFirstName());
        existingStoreOwner.setMiddleName(storeOwner.getMiddleName());
        existingStoreOwner.setLastName(storeOwner.getLastName());
        existingStoreOwner.setEmail(storeOwner.getEmail());
        existingStoreOwner.setPhone(storeOwner.getPhone());
        existingStoreOwner.setAddress(storeOwner.getAddress());
        existingStoreOwner.setIsActive(storeOwner.getIsActive());

        // TODO: Add Validations
        return storeOwnerRepository.save(existingStoreOwner);
    }

    @Override
    public void activateStoreOwner(Long customerId) throws EntityNotFoundException, InvalidActionException {
        StoreOwner owner = getStoreOwnerById(customerId);
        if(owner.getIsActive()) {
            throw new InvalidActionException("StoreOwner is already active");
        }

        owner.setIsActive(true);

        // TODO: Add Validations
        storeOwnerRepository.save(owner);
    }

    @Override
    public void deactivateStoreOwner(Long customerId) throws EntityNotFoundException, InvalidActionException {
        StoreOwner owner = getStoreOwnerById(customerId);
        if(!owner.getIsActive()) {
            throw new InvalidActionException("StoreOwner is already inactive");
        }

        owner.setIsActive(false);

        // TODO: Add Validations
        storeOwnerRepository.save(owner);
    }
}
