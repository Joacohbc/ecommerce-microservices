package com.microecommerce.customerservice.controllers;

import com.microecommerce.customerservice.models.Buyer;
import com.microecommerce.customerservice.models.StoreOwner;
import com.microecommerce.customerservice.services.interfaces.IBuyerService;
import com.microecommerce.utilitymodule.exceptions.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers/buyers")
public class BuyerController {
    private final IBuyerService customerService;

    public BuyerController(IBuyerService customerService) {
        this.customerService = customerService;
    }

    // Buyer Endpoints

    @PostMapping("/buyer")
    public ResponseEntity<Buyer> createBuyer(@RequestBody Buyer buyer) {
        Buyer createdBuyer = customerService.createBuyer(buyer);
        return new ResponseEntity<>(createdBuyer, HttpStatus.CREATED);
    }

    @GetMapping("/buyer/{customerId}")
    public ResponseEntity<Buyer> getBuyerById(@PathVariable Long customerId) throws EntityNotFoundException {
        Buyer buyer = customerService.getBuyerById(customerId);
        return buyer != null ? new ResponseEntity<>(buyer, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/buyer/{customerId}")
    public ResponseEntity<Buyer> updateBuyer(@PathVariable Long customerId, @RequestBody Buyer buyer) throws EntityNotFoundException {
        Buyer updatedBuyer = customerService.updateBuyer(customerId, buyer);
        return updatedBuyer != null ? new ResponseEntity<>(updatedBuyer, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Customer Activation/Deactivation Endpoints

    @PutMapping("/buyer/{customerId}/activate")
    public ResponseEntity<Void> activateBuyer(@PathVariable Long customerId) throws EntityNotFoundException {
        customerService.activateBuyer(customerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/buyer/{customerId}/deactivate")
    public ResponseEntity<Void> deactivateBuyer(@PathVariable Long customerId) throws EntityNotFoundException {
        customerService.deactivateBuyer(customerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Existing Customer to Buyer/Store Owner Endpoints

    @PostMapping("/buyer/create-from/{customerId}")
    public ResponseEntity<Buyer> createExistingCustomerAsBuyer(@PathVariable Long customerId, @RequestBody Buyer buyer) throws EntityNotFoundException {
        Buyer createdBuyer = customerService.createExistingCustomerAsBuyer(customerId, buyer);
        return new ResponseEntity<>(createdBuyer, HttpStatus.CREATED);
    }
}
