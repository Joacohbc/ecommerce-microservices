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

    @PostMapping("/buyers")
    public ResponseEntity<Buyer> createBuyer(@RequestBody Buyer buyer) {
        Buyer createdBuyer = customerService.createBuyer(buyer);
        return new ResponseEntity<>(createdBuyer, HttpStatus.CREATED);
    }

    @GetMapping("/buyers/{customerId}")
    public ResponseEntity<Buyer> getBuyerById(@PathVariable Long customerId) {
        Buyer buyer = customerService.getBuyerById(customerId);
        return buyer != null ? new ResponseEntity<>(buyer, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/buyers/email/{email}")
    public ResponseEntity<Buyer> getBuyerByEmail(@PathVariable String email) {
        Buyer buyer = customerService.getBuyerByEmail(email);
        return buyer != null ? new ResponseEntity<>(buyer, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/buyers/{customerId}")
    public ResponseEntity<Buyer> updateBuyer(@PathVariable Long customerId, @RequestBody Buyer buyer) {
        Buyer updatedBuyer = customerService.updateBuyer(customerId, buyer);
        return updatedBuyer != null ? new ResponseEntity<>(updatedBuyer, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/buyers/{customerId}")
    public ResponseEntity<Void> deleteBuyer(@PathVariable Long customerId) {
        customerService.deleteBuyer(customerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Store Owner Endpoints

    @PostMapping("/store-owners")
    public ResponseEntity<StoreOwner> createStoreOwner(@RequestBody StoreOwner storeOwner) {
        StoreOwner createdStoreOwner = customerService.createStoreOwner(storeOwner);
        return new ResponseEntity<>(createdStoreOwner, HttpStatus.CREATED);
    }

    @GetMapping("/store-owners/{customerId}")
    public ResponseEntity<StoreOwner> getStoreOwnerById(@PathVariable Long customerId) {
        StoreOwner storeOwner = customerService.getStoreOwnerById(customerId);
        return storeOwner != null ? new ResponseEntity<>(storeOwner, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/store-owners/email/{email}")
    public ResponseEntity<StoreOwner> getStoreOwnerByEmail(@PathVariable String email) {
        StoreOwner storeOwner = customerService.getStoreOwnerByEmail(email);
        return storeOwner != null ? new ResponseEntity<>(storeOwner, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/store-owners/{customerId}")
    public ResponseEntity<StoreOwner> updateStoreOwner(@PathVariable Long customerId, @RequestBody StoreOwner storeOwner) {
        StoreOwner updatedStoreOwner = customerService.updateStoreOwner(customerId, storeOwner);
        return updatedStoreOwner != null ? new ResponseEntity<>(updatedStoreOwner, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/store-owners/{customerId}")
    public ResponseEntity<Void> deleteStoreOwner(@PathVariable Long customerId) {
        customerService.deleteStoreOwner(customerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Customer Activation/Deactivation Endpoints

    @PutMapping("/{customerId}/activate")
    public ResponseEntity<Void> activateCustomer(@PathVariable Long customerId) {
        customerService.activateCustomer(customerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{customerId}/deactivate")
    public ResponseEntity<Void> deactivateCustomer(@PathVariable Long customerId) {
        customerService.deactivateCustomer(customerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Existing Customer to Buyer/Store Owner Endpoints

    @PostMapping("/{customerId}/buyer")
    public ResponseEntity<Buyer> createExistingCustomerAsBuyer(@PathVariable Long customerId, @RequestBody Buyer buyer) {
        Buyer createdBuyer = customerService.createExistingCustomerAsBuyer(customerId, buyer);
        return new ResponseEntity<>(createdBuyer, HttpStatus.CREATED);
    }

    @PostMapping("/{customerId}/store-owner")
    public ResponseEntity<StoreOwner> createExistingCustomerAsStoreOwner(@PathVariable Long customerId, @RequestBody StoreOwner storeOwner) {
        StoreOwner createdStoreOwner = customerService.createExistingCustomerAsStoreOwner(customerId, storeOwner);
        return new ResponseEntity<>(createdStoreOwner, HttpStatus.CREATED);
    }
}