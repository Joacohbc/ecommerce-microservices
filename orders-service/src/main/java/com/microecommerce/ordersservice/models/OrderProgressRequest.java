package com.microecommerce.ordersservice.models;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class OrderProgressRequest implements Serializable  {
    @NotBlank
    private String orderId;
    private Long paymentId;
    private Long discountId;
    private Long shippingId;
    private String message;
}
