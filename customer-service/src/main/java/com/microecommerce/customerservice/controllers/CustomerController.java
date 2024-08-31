package com.microecommerce.customerservice.controllers;

import com.microecommerce.customerservice.models.Buyer;
import com.microecommerce.customerservice.models.StoreOwner;
import com.microecommerce.customerservice.services.interfaces.ICustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final ICustomerService customerService;

    public CustomerController(ICustomerService customerService) {
        this.customerService = customerService;
    }

    // Buyer Endpoints

    @PostMapping("/buyer")
    public ResponseEntity<Buyer> createBuyer(@RequestBody Buyer buyer) {
        Buyer createdBuyer = customerService.createBuyer(buyer);
        return new ResponseEntity<>(createdBuyer, HttpStatus.CREATED);
    }

    @GetMapping("/buyer/{customerId}")
    public ResponseEntity<Buyer> getBuyerById(@PathVariable Long customerId) {
        Buyer buyer = customerService.getBuyerById(customerId);
        return buyer != null ? new ResponseEntity<>(buyer, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/buyer/{customerId}")
    public ResponseEntity<Buyer> updateBuyer(@PathVariable Long customerId, @RequestBody Buyer buyer) {
        Buyer updatedBuyer = customerService.updateBuyer(customerId, buyer);
        return updatedBuyer != null ? new ResponseEntity<>(updatedBuyer, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Store Owner Endpoints

    @PostMapping("/store-owner")
    public ResponseEntity<StoreOwner> createStoreOwner(@RequestBody StoreOwner storeOwner) {
        StoreOwner createdStoreOwner = customerService.createStoreOwner(storeOwner);
        return new ResponseEntity<>(createdStoreOwner, HttpStatus.CREATED);
    }

    @GetMapping("/store-owner/{customerId}")
    public ResponseEntity<StoreOwner> getStoreOwnerById(@PathVariable Long customerId) {
        StoreOwner storeOwner = customerService.getStoreOwnerById(customerId);
        return storeOwner != null ? new ResponseEntity<>(storeOwner, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/store-owner/{customerId}")
    public ResponseEntity<StoreOwner> updateStoreOwner(@PathVariable Long customerId, @RequestBody StoreOwner storeOwner) {
        StoreOwner updatedStoreOwner = customerService.updateStoreOwner(customerId, storeOwner);
        return updatedStoreOwner != null ? new ResponseEntity<>(updatedStoreOwner, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/store-owner/{customerId}")
    public ResponseEntity<Void> deleteStoreOwner(@PathVariable Long customerId) {
        customerService.deleteStoreOwner(customerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Customer Activation/Deactivation Endpoints

    @PutMapping("/buyer/{customerId}/activate")
    public ResponseEntity<Void> activateBuyer(@PathVariable Long customerId) {
        customerService.activateBuyer(customerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/buyer/{customerId}/deactivate")
    public ResponseEntity<Void> deactivateBuyer(@PathVariable Long customerId) {
        customerService.deactivateBuyer(customerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/store-owner/{customerId}/activate")
    public ResponseEntity<Void> activateStoreOwner(@PathVariable Long customerId) {
        customerService.activateStoreOwner(customerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/store-owner/{customerId}/deactivate")
    public ResponseEntity<Void> deactivateStoreOwner(@PathVariable Long customerId) {
        customerService.deactivateStoreOwner(customerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Existing Customer to Buyer/Store Owner Endpoints

    @PostMapping("/buyer/create-from/{customerId}")
    public ResponseEntity<Buyer> createExistingCustomerAsBuyer(@PathVariable Long customerId, @RequestBody Buyer buyer) {
        Buyer createdBuyer = customerService.createExistingCustomerAsBuyer(customerId, buyer);
        return new ResponseEntity<>(createdBuyer, HttpStatus.CREATED);
    }

    @PostMapping("/store-owner/create-from/{customerId}")
    public ResponseEntity<StoreOwner> createExistingCustomerAsStoreOwner(@PathVariable Long customerId, @RequestBody StoreOwner storeOwner) {
        StoreOwner createdStoreOwner = customerService.createExistingCustomerAsStoreOwner(customerId, storeOwner);
        return new ResponseEntity<>(createdStoreOwner, HttpStatus.CREATED);
    }
}