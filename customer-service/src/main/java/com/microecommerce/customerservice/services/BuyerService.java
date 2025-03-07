package com.microecommerce.customerservice.services;

import com.microecommerce.customerservice.models.Buyer;
import com.microecommerce.customerservice.models.Customer;
import com.microecommerce.customerservice.repositories.BuyerRepository;
import com.microecommerce.customerservice.repositories.CustomerRepository;
import com.microecommerce.customerservice.services.interfaces.IBuyerService;
import com.microecommerce.utilitymodule.exceptions.EntityNotFoundException;
import com.microecommerce.utilitymodule.exceptions.InvalidActionException;
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

        // TODO: Add Validations
        return buyerRepository.save(buyer);
    }

    @Override
    public Buyer getBuyerByEmail(String email) throws EntityNotFoundException {
        return Optional.of(buyerRepository.findByEmail(email))
                .orElseThrow(() -> new EntityNotFoundException("Buyer with Email " + email + " not found"));
    }

    @Override
    public Buyer getBuyerById(Long customerId) throws EntityNotFoundException {
        return buyerRepository
                .findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Buyer with the Id " + customerId + " not found"));
    }

    @Override
    public Buyer updateBuyer(Long customerId, Buyer buyer) throws EntityNotFoundException {
        Buyer existingBuyer = getBuyerById(customerId);
        existingBuyer.setFirstName(buyer.getFirstName());
        existingBuyer.setMiddleName(buyer.getMiddleName());
        existingBuyer.setLastName(buyer.getLastName());
        existingBuyer.setEmail(buyer.getEmail());
        existingBuyer.setPhone(buyer.getPhone());
        existingBuyer.setAddress(buyer.getAddress());
        existingBuyer.setIsActive(buyer.getIsActive());

        // TODO: Add Validations
        return buyerRepository.save(existingBuyer);
    }

    @Override
    public void activateBuyer(Long customerId) throws EntityNotFoundException, InvalidActionException {
        Buyer buyer = getBuyerById(customerId);
        if(buyer.getIsActive()) {
            throw new InvalidActionException("Buyer is already active");
        }

        buyer.setIsActive(true);

        // TODO: Add Validations
        buyerRepository.save(buyer);
    }

    @Override
    public void deactivateBuyer(Long customerId) throws EntityNotFoundException, InvalidActionException {
        Buyer buyer = getBuyerById(customerId);
        if(!buyer.getIsActive()) {
            throw new InvalidActionException("Buyer is already inactive");
        }

        buyer.setIsActive(false);

        // TODO: Add Validations
        buyerRepository.save(buyer);
    }
}
