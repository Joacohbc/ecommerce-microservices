package com.microecommerce.customerservice.services;

import com.microecommerce.customerservice.models.Buyer;
import com.microecommerce.customerservice.models.Customer;
import com.microecommerce.customerservice.repositories.BuyerRepository;
import com.microecommerce.customerservice.repositories.CustomerRepository;
import com.microecommerce.customerservice.services.interfaces.IBuyerService;
import com.microecommerce.utilitymodule.exceptions.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BuyerService implements IBuyerService {

    private final BuyerRepository buyerRepository;
    private final CustomerRepository customerRepository;

    public BuyerService(BuyerRepository buyerRepository, CustomerRepository customerRepository) {
        this.buyerRepository = buyerRepository;
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
        Buyer existingBuyer = buyerRepository
                .findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Buyer not found"));

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
        }
    }

    @Override
    public void deactivateBuyer(Long customerId) {
        Optional<Buyer> buyer = buyerRepository.findById(customerId);
        if (buyer.isPresent()) {
            buyer.get().setIsActive(false);
            buyerRepository.save(buyer.get());
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

}
