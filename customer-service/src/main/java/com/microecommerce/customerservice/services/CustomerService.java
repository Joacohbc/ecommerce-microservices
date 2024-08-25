package com.microecommerce.customerservice.services;

import com.microecommerce.customerservice.models.Buyer;
import com.microecommerce.customerservice.models.Customer;
import com.microecommerce.customerservice.models.StoreOwner;
import com.microecommerce.customerservice.repositories.BuyerRepository;
import com.microecommerce.customerservice.repositories.CustomerRepository;
import com.microecommerce.customerservice.repositories.StoreOwnerRepository;
import com.microecommerce.customerservice.services.interfaces.ICustomerService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService implements ICustomerService {

    private BuyerRepository buyerRepository;
    private StoreOwnerRepository storeOwnerRepository;
    private CustomerRepository customerRepository;

    public CustomerService(BuyerRepository buyerRepository, StoreOwnerRepository storeOwnerRepository, CustomerRepository customerRepository) {
        this.buyerRepository = buyerRepository;
        this.storeOwnerRepository = storeOwnerRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public Buyer createBuyer(Buyer buyer) {
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
    public Buyer updateBuyer(Long customerId, Buyer buyer) {
        Optional<Buyer> existingBuyer = buyerRepository.findById(customerId);
        if (existingBuyer.isPresent()) {
            Buyer updatedBuyer = existingBuyer.get();
            updatedBuyer.setFirstName(buyer.getFirstName());
            updatedBuyer.setMiddleName(buyer.getMiddleName());
            updatedBuyer.setLastName(buyer.getLastName());
            updatedBuyer.setEmail(buyer.getEmail());
            updatedBuyer.setPhone(buyer.getPhone());
            updatedBuyer.setAddress(buyer.getAddress());
            updatedBuyer.setIsActive(buyer.getIsActive());
            return buyerRepository.save(updatedBuyer);
        }
        return null;
    }

    @Override
    public void deleteBuyer(Long customerId) {
        buyerRepository.deleteById(customerId);
    }

    @Override
    public StoreOwner createStoreOwner(StoreOwner storeOwner) {
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
        if (!existingStoreOwner.isPresent()) return null;
        
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
    public void deleteStoreOwner(Long customerId) {
        storeOwnerRepository.deleteById(customerId);
    }

    @Override
    public void activateCustomer(Long customerId) {
        // Assuming Buyer and StoreOwner have the isActive field
        Optional<Buyer> buyer = buyerRepository.findById(customerId);
        if (buyer.isPresent()) {
            buyer.get().setIsActive(true);
            buyerRepository.save(buyer.get());
            return;
        }

        Optional<StoreOwner> storeOwner = storeOwnerRepository.findById(customerId);
        if (storeOwner.isPresent()) {
            storeOwner.get().setIsActive(true);
            storeOwnerRepository.save(storeOwner.get());
        }
    }

    @Override
    public void deactivateCustomer(Long customerId) {
        // Assuming Buyer and StoreOwner have the isActive field
        Optional<Buyer> buyer = buyerRepository.findById(customerId);
        if (buyer.isPresent()) {
            buyer.get().setIsActive(false);
            buyerRepository.save(buyer.get());
            return;
        }

        Optional<StoreOwner> storeOwner = storeOwnerRepository.findById(customerId);
        if (storeOwner.isPresent()) {
            storeOwner.get().setIsActive(false);
            storeOwnerRepository.save(storeOwner.get());
        }
    }

    @Override
    public Buyer createExistingCustomerAsBuyer(Long customerId, Buyer buyer) {
        Optional<Customer> existingCustomer = customerRepository.findById(customerId);
        if (existingCustomer.isPresent()) {
            buyer.setCustomerId(existingCustomer.get().getCustomerId());
            buyer.setCredentialsId(existingCustomer.get().getCredentialsId());
            buyer.setFirstName(existingCustomer.get().getFirstName());
            buyer.setMiddleName(existingCustomer.get().getMiddleName());
            buyer.setLastName(existingCustomer.get().getLastName());
            buyer.setEmail(existingCustomer.get().getEmail());
            buyer.setPhone(existingCustomer.get().getPhone());
            buyer.setAddress(existingCustomer.get().getAddress());
            return buyerRepository.save(buyer);
        }
        return null;
    }

    @Override
    public StoreOwner createExistingCustomerAsStoreOwner(Long customerId, StoreOwner storeOwner) {
        Optional<Customer> existingCustomer = customerRepository.findById(customerId);
        if (existingCustomer.isPresent()) {
            storeOwner.setCustomerId(existingCustomer.get().getCustomerId());
            storeOwner.setCredentialsId(existingCustomer.get().getCredentialsId());
            storeOwner.setFirstName(existingCustomer.get().getFirstName());
            storeOwner.setMiddleName(existingCustomer.get().getMiddleName());
            storeOwner.setLastName(existingCustomer.get().getLastName());
            storeOwner.setEmail(existingCustomer.get().getEmail());
            storeOwner.setPhone(existingCustomer.get().getPhone());
            storeOwner.setAddress(existingCustomer.get().getAddress());
            return storeOwnerRepository.save(storeOwner);
        }
        return null;
    }
}