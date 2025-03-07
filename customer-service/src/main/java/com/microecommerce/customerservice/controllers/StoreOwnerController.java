package com.microecommerce.customerservice.controllers;

import com.microecommerce.customerservice.models.StoreOwner;
import com.microecommerce.customerservice.services.interfaces.IStoreOwnerService;
import com.microecommerce.utilitymodule.exceptions.EntityNotFoundException;
import com.microecommerce.utilitymodule.exceptions.InvalidActionException;
import com.microecommerce.utilitymodule.exceptions.RestExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/store-owner")
public class StoreOwnerController {
    private final IStoreOwnerService customerService;

    public StoreOwnerController(IStoreOwnerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/")
    public ResponseEntity<StoreOwner> createStoreOwner(@RequestBody StoreOwner storeOwner) {
        StoreOwner createdStoreOwner = customerService.createStoreOwner(storeOwner);
        return new ResponseEntity<>(createdStoreOwner, HttpStatus.CREATED);
    }

    @GetMapping("/{customerId}")
    public StoreOwner getStoreOwnerById(@PathVariable Long customerId) throws EntityNotFoundException {
        return customerService.getStoreOwnerById(customerId);
    }

    @PutMapping("/{customerId}")
    public StoreOwner updateStoreOwner(@PathVariable Long customerId, @RequestBody StoreOwner storeOwner) throws EntityNotFoundException {
        return customerService.updateStoreOwner(customerId, storeOwner);
    }

    @PutMapping("/{customerId}/activate")
    public Map<String, Object> activateStoreOwner(@PathVariable Long customerId) throws EntityNotFoundException, InvalidActionException {
        customerService.activateStoreOwner(customerId);
        return RestExceptionHandler.createJsonResponse("Store owner activated successfully", null, HttpStatus.OK);
    }

    @PutMapping("/{customerId}/deactivate")
    public Map<String, Object> deactivateStoreOwner(@PathVariable Long customerId) throws EntityNotFoundException, InvalidActionException {
        customerService.deactivateStoreOwner(customerId);
        return RestExceptionHandler.createJsonResponse("Store owner deactivated successfully", null, HttpStatus.OK);
    }
}
