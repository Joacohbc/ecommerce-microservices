package com.microecommerce.customerservice.controllers;

import com.microecommerce.customerservice.models.Buyer;
import com.microecommerce.customerservice.models.StoreOwner;
import com.microecommerce.customerservice.services.interfaces.IStoreOwnerService;
import com.microecommerce.utilitymodule.exceptions.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers/buyers")
public class StoreOwnerController {
    private final IStoreOwnerService customerService;

    public StoreOwnerController(IStoreOwnerService customerService) {
        this.customerService = customerService;
    }

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

    @PostMapping("/store-owner/create-from/{customerId}")
    public ResponseEntity<StoreOwner> createExistingCustomerAsStoreOwner(@PathVariable Long customerId, @RequestBody StoreOwner storeOwner) {
        StoreOwner createdStoreOwner = customerService.createExistingCustomerAsStoreOwner(customerId, storeOwner);
        return new ResponseEntity<>(createdStoreOwner, HttpStatus.CREATED);
    }
}
