package com.microecommerce.customerservice.controllers;

import com.microecommerce.customerservice.models.Buyer;
import com.microecommerce.customerservice.services.interfaces.IBuyerService;
import com.microecommerce.utilitymodule.exceptions.EntityNotFoundException;
import com.microecommerce.utilitymodule.exceptions.InvalidActionException;
import com.microecommerce.utilitymodule.exceptions.RestExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/buyer")
public class BuyerController {
    private final IBuyerService customerService;

    public BuyerController(IBuyerService customerService) {
        this.customerService = customerService;
    }

    // Buyer Endpoints

    @PostMapping("/")
    public ResponseEntity<Buyer> createBuyer(@RequestBody Buyer buyer) {
        Buyer createdBuyer = customerService.createBuyer(buyer);
        return new ResponseEntity<>(createdBuyer, HttpStatus.CREATED);
    }

    @GetMapping("/{customerId}")
    public Buyer getBuyerById(@PathVariable Long customerId) throws EntityNotFoundException {
        return customerService.getBuyerById(customerId);
    }

    @PutMapping("/{customerId}")
    public Buyer updateBuyer(@PathVariable Long customerId, @RequestBody Buyer buyer) throws EntityNotFoundException {
        return customerService.updateBuyer(customerId, buyer);
    }

    // Customer Activation/Deactivation Endpoints

    @PutMapping("/{customerId}/activate")
    public Map<String, Object> activateBuyer(@PathVariable Long customerId) throws EntityNotFoundException, InvalidActionException {
        customerService.activateBuyer(customerId);
        return RestExceptionHandler.createJsonResponse("Buyer activated successfully", null, HttpStatus.OK);
    }

    @PutMapping("/{customerId}/deactivate")
    public Map<String, Object> deactivateBuyer(@PathVariable Long customerId) throws EntityNotFoundException, InvalidActionException {
        customerService.deactivateBuyer(customerId);
        return RestExceptionHandler.createJsonResponse("Buyer deactivated successfully", null, HttpStatus.OK);
    }
}
