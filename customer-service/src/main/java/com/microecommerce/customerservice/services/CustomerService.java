package com.microecommerce.customerservice.services;

import com.microecommerce.customerservice.models.Buyer;
import com.microecommerce.customerservice.models.Customer;
import com.microecommerce.customerservice.models.StoreOwner;
import com.microecommerce.customerservice.repositories.BuyerRepository;
import com.microecommerce.customerservice.repositories.CustomerRepository;
import com.microecommerce.customerservice.repositories.StoreOwnerRepository;
import com.microecommerce.customerservice.services.interfaces.ICustomerService;
import com.microecommerce.utilitymodule.exceptions.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService implements ICustomerService {

    private final BuyerRepository buyerRepository;
    private final StoreOwnerRepository storeOwnerRepository;
    private final CustomerRepository customerRepository;

    public CustomerService(BuyerRepository buyerRepository, StoreOwnerRepository storeOwnerRepository, CustomerRepository customerRepository) {
        this.buyerRepository = buyerRepository;
        this.storeOwnerRepository = storeOwnerRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public Buyer createBuyer(Buyer buyer) {
        buyer.setIsActive(false);
        return buyerRepository.save(buyer);
    }

    @Override
    public Buyer getBuyerByEmail(String email) {
        return buyerRepository.findByEmail(email);
    }

    @Override
    public Buyer getBuyerById(Long customerId) {
        return buyerRepository.findByCustomerId(customerId);
    }

    @Override
    public Buyer updateBuyer(Long customerId, Buyer buyer) throws EntityNotFoundException {
        Buyer existingBuyer = buyerRepository.findById(customerId).orElseThrow(() -> new EntityNotFoundException("Buyer not found"));
        existingBuyer.setFirstName(buyer.getFirstName());
        existingBuyer.setMiddleName(buyer.getMiddleName());
        existingBuyer.setLastName(buyer.getLastName());
        existingBuyer.setEmail(buyer.getEmail());
        existingBuyer.setPhone(buyer.getPhone());
        existingBuyer.setAddress(buyer.getAddress());
        existingBuyer.setIsActive(buyer.getIsActive());
        return buyerRepository.save(existingBuyer);
    }

    @Override
    public void activateBuyer(Long customerId) {
        Optional<Buyer> buyer = buyerRepository.findById(customerId);
        if (buyer.isPresent()) {
            buyer.get().setIsActive(true);
            buyerRepository.save(buyer.get());
            return;
        }
    }

    @Override
    public void deactivateBuyer(Long customerId) {
        Optional<Buyer> buyer = buyerRepository.findById(customerId);
        if (buyer.isPresent()) {
            buyer.get().setIsActive(false);
            buyerRepository.save(buyer.get());
            return;
        }
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
    public StoreOwner updateStoreOwner(Long customerId, StoreOwner storeOwner) {
        Optional<StoreOwner> existingStoreOwner = storeOwnerRepository.findById(customerId);
        if (existingStoreOwner.isEmpty()) return null;
        
        StoreOwner updatedStoreOwner = existingStoreOwner.get();
        updatedStoreOwner.setFirstName(storeOwner.getFirstName());
        updatedStoreOwner.setMiddleName(storeOwner.getMiddleName());
        updatedStoreOwner.setLastName(storeOwner.getLastName());
        updatedStoreOwner.setEmail(storeOwner.getEmail());
        updatedStoreOwner.setPhone(storeOwner.getPhone());
        updatedStoreOwner.setAddress(storeOwner.getAddress());
        updatedStoreOwner.setIsActive(storeOwner.getIsActive());
        return storeOwnerRepository.save(updatedStoreOwner);
    }

    @Override
    public void activateStoreOwner(Long customerId) {
        Optional<StoreOwner> buyer = storeOwnerRepository.findById(customerId);
        if (buyer.isPresent()) {
            buyer.get().setIsActive(true);
            storeOwnerRepository.save(buyer.get());
            return;
        }
    }

    @Override
    public void deactivateStoreOwner(Long customerId) {
        Optional<StoreOwner> buyer = storeOwnerRepository.findById(customerId);
        if (buyer.isPresent()) {
            buyer.get().setIsActive(false);
            storeOwnerRepository.save(buyer.get());
            return;
        }
    }

    @Override
    public Buyer createExistingCustomerAsBuyer(Long customerId, Buyer buyer) {
        Optional<Customer> existingCustomer = customerRepository.findById(customerId);
        if (existingCustomer.isPresent()) {
            Buyer newBuyer = new Buyer();
            newBuyer.setCustomerId(existingCustomer.get().getCustomerId());
            newBuyer.setCredentialsId(existingCustomer.get().getCredentialsId());
            newBuyer.setFirstName(existingCustomer.get().getFirstName());
            newBuyer.setMiddleName(existingCustomer.get().getMiddleName());
            newBuyer.setLastName(existingCustomer.get().getLastName());
            newBuyer.setEmail(existingCustomer.get().getEmail());
            newBuyer.setPhone(existingCustomer.get().getPhone());
            newBuyer.setAddress(existingCustomer.get().getAddress());

            // Specific fields of Buyer
            newBuyer.setIsActive(true);
            return buyerRepository.save(buyer);
        }
        return null;
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