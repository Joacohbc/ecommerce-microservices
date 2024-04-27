package com.microecommerce.ordersservice.services;

import com.microecommerce.ordersservice.models.*;
import com.microecommerce.ordersservice.repositories.OrderHistoryRepository;
import com.microecommerce.ordersservice.repositories.OrderRepository;
import com.microecommerce.ordersservice.services.interfaces.IOrderService;
import com.microecommerce.utilitymodule.exceptions.InvalidEntityException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;

@Service
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final OrderHistoryRepository orderHistoryRepository;
    private final ProductFeignService productService;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderHistoryRepository orderHistoryRepository, ProductFeignService productService) {
        this.orderRepository = orderRepository;
        this.orderHistoryRepository = orderHistoryRepository;
        this.productService = productService;
    }

    @Override
    public List<Order> createBatch(@Valid List<Order> orders, Long customerId) throws InvalidEntityException {
        boolean productsExistence = productService.checkProductsExistence(orders
                .stream()
                .flatMap(order -> order.getItems().stream().map(OrderItem::getProductId))
                .toList());

        if(!productsExistence) throw new InvalidEntityException("Some products do not exist");


        List<OrderHistory> orderHistories = new ArrayList<>(orders.size());
        for(Order order : orders) {
            validateOrderItems(order);
            order.setStatus(OrderStatus.CREATED);
            order.setCustomerId(customerId);
            orderHistories.add(createOrderHistory(order, null, ""));
        }
        orderHistoryRepository.saveAll(orderHistories);

        return orderRepository.insert(orders);
    }

    @Override
    public Order askForReturn(String orderId) throws InvalidEntityException {
        // TODO: Implement the logic for asking for a return
        return updateStatus(orderId, OrderStatus.RETURN_REQUESTED);
    }

    @Override
    public Order askForRefund(String orderId) throws InvalidEntityException {
        // TODO: Implement the logic for asking for a refund
        return updateStatus(orderId, OrderStatus.REFUND_PENDING);
    }

    @Override
    public Order askForCancellation(String orderId) throws InvalidEntityException {
        // TODO: Implement the logic for asking for a cancellation
        return updateStatus(orderId, OrderStatus.CANCELLED);
    }

    @Override
    public Order finishOrder(String orderId, OrderStatus status) throws InvalidEntityException {
        if(status != OrderStatus.INVOICE_ISSUED
                && status != OrderStatus.RETURN_RECEIVED
                && status != OrderStatus.REFUND_COMPLETED
                && status != OrderStatus.CANCELLED) {
            throw new InvalidEntityException("The status " + status + " is not allowed for finishing an order");
        }

        Order order = getById(orderId);
        validateStatusChange(order.getStatus(), status);
        order.setStatus(status);
        order.setFinishedAt(LocalDateTime.now());

        // TODO: Validate Payment and Shipping details, and update the order accordingly

        orderRepository.save(order);
        orderHistoryRepository.save(createOrderHistory(order, status, "Finished"));

        return order;
    }

    @Override
    public List<Order> putInProgress(@Valid List<OrderProgressRequest> requests) throws InvalidEntityException {
        List<Order> orders = orderRepository.findAllById(requests.stream().map(OrderProgressRequest::getOrderId).toList());

        // TODO: Implement the logic for processing the order (payment, picking, packing, etc.)

        List<OrderHistory> orderHistories = new LinkedList<>();
        for (Order order : orders) {

            // Validate the order
            validateOrderItems(order);
            validateStatusChange(order.getStatus(), OrderStatus.IN_PROGRESS);

            // Update the order status
            order.setStatus(OrderStatus.IN_PROGRESS);
            order.setProcessedAt(LocalDateTime.now());

            // Get the progress request for the order
            OrderProgressRequest progressRequest = requests.stream()
                    .filter(request -> request.getOrderId().equals(order.getId()))
                    .findFirst()
                    .get();

            // Set the payment ids
            order.setPaymentId(Optional.of(progressRequest.getPaymentId())
                    .orElseThrow(() -> new InvalidEntityException("Payment IDs are required")));

            if(progressRequest.getShippingId() != null)
                order.setShippingId(progressRequest.getShippingId());

            if(progressRequest.getDiscountId() != null)
                order.setDiscountId(progressRequest.getDiscountId());

            // TODO: Call async services for payment, shipping, and discount processing
            // Create an order history entry
            orderHistories.add(createOrderHistory(order, OrderStatus.IN_PROGRESS, progressRequest.getMessage()));
        }

        orderRepository.saveAll(orders);
        orderHistoryRepository.saveAll(orderHistories);
        return orders;
    }

    @Override
    public Order getById(String orderId) throws InvalidEntityException {
        return orderRepository
                .findById(orderId)
                .orElseThrow(() -> new InvalidEntityException("Order with id " + orderId + " does not exist"));
    }

    @Override
    public List<Order> getAllCustomerOrders(Long customerId) {
        return orderRepository.findAllByCustomerId(customerId);
    }

    // TODO: ONLY FOR TESTING PURPOSES
    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // Update the status after the order has been processed and create an order history entry
    private Order updateStatus(String orderId, OrderStatus status) throws InvalidEntityException {
        Order order = getById(orderId);

        validateStatusChange(order.getStatus(), status);
        order.setStatus(status);

        orderHistoryRepository.save(createOrderHistory(order, status, ""));
        return orderRepository.save(order);
    }

    // Validate the order
    // If the order is null, throw an InvalidEntityException with the message "Order is null"
    // If the order has no items, throw an InvalidEntityException with the message "Order {orderId} has no items"
    // If any item in the order has a quantity less than or equal to 0, throw an InvalidEntityException with the message "Order {orderId} has invalid quantity for item {productId}"
    private void validateOrderItems(Order order) throws InvalidEntityException {
        if(order.getItems().isEmpty()) {
            throw new InvalidEntityException("Order " + order.getId() + " has no items");
        }

        for (OrderItem item : order.getItems()) {
            if(item.getQuantity() <= 0) {
                throw new InvalidEntityException("Order " + order.getId() + " has invalid quantity for item " + item.getProductId());
            }
        }
    }

    // Validate the status change
    // If the current status is CREATED, the only valid next statuses are IN_PROGRESS and CANCELLED
    // If the current status is IN_PROGRESS, the only valid next statuses are CANCELLED, INVOICE_ISSUED, RETURN_REQUESTED, and REFUND_PENDING
    // If the current status is REFUND_PENDING, the only valid next status is REFUND_COMPLETED
    // If the current status is RETURN_REQUESTED, the only valid next status is RETURN_RECEIVED
    // If the current status is CANCELLED, REFUND_COMPLETED, RETURN_RECEIVED, or INVOICE_ISSUED, no status change is allowed
    private void validateStatusChange(OrderStatus currentStatus, OrderStatus nextStatus) throws InvalidEntityException {
        if(currentStatus == OrderStatus.CANCELLED) {
            throw new InvalidEntityException("Order is already cancelled");
        }

        if(currentStatus == OrderStatus.REFUND_COMPLETED) {
            throw new InvalidEntityException("Order is already refunded");
        }

        if(currentStatus == OrderStatus.RETURN_RECEIVED) {
            throw new InvalidEntityException("Order is already returned");
        }

        if(currentStatus == OrderStatus.INVOICE_ISSUED) {
            throw new InvalidEntityException("Order is already invoiced");
        }

        // If the current status is CREATED, the only valid next statuses are IN_PROGRESS and CANCELLED
        if(currentStatus == OrderStatus.CREATED) {
            if(nextStatus == OrderStatus.IN_PROGRESS
                    || nextStatus == OrderStatus.CANCELLED) return;
            throw new InvalidEntityException("Order processing has not started yet");
        }

        // If the current status is IN_PROGRESS, the only valid next statuses are CANCELLED, INVOICE_ISSUED, RETURN_REQUESTED, and REFUND_PENDING
        if(currentStatus == OrderStatus.IN_PROGRESS) {
            if(nextStatus == OrderStatus.CANCELLED
                    || nextStatus == OrderStatus.INVOICE_ISSUED
                    || nextStatus == OrderStatus.RETURN_REQUESTED
                    || nextStatus == OrderStatus.REFUND_PENDING) return;
            throw new InvalidEntityException("Order processing is still in progress");
        }

        if(currentStatus == OrderStatus.REFUND_PENDING && nextStatus != OrderStatus.REFUND_COMPLETED) {
            throw new InvalidEntityException("Order refund is still pending");
        }

        if(currentStatus == OrderStatus.RETURN_REQUESTED && nextStatus != OrderStatus.RETURN_RECEIVED) {
            throw new InvalidEntityException("Order return is still pending");
        }
    }

    // Create an order history entry
    private OrderHistory createOrderHistory(Order order, OrderStatus nextStatus, String additionalMessage) {
        return OrderHistory.builder()
                .orderId(order.getId())
                .status(nextStatus)
                .description(getStatusMessageChange(order.getStatus(), nextStatus, additionalMessage))
                .build();
    }

    // Get the status message change
    private String getStatusMessageChange(OrderStatus currentStatus, OrderStatus nextStatus, String additionalMessage) {
        Function<String, String> getMessage = (message) ->
                additionalMessage != null && !additionalMessage.isBlank()
                        ? message + " - " + additionalMessage
                        : message;

        if(currentStatus == OrderStatus.CREATED && nextStatus == null) return getMessage.apply("Order submitted by customer, awaiting further processing");

        if(currentStatus == OrderStatus.CREATED) {
            if(nextStatus == OrderStatus.IN_PROGRESS) return getMessage.apply("Order processing initiated (picking, packing)");
            if(nextStatus == OrderStatus.CANCELLED) return getMessage.apply("Order cancelled");
        }

        if(currentStatus == OrderStatus.IN_PROGRESS) {
            if(nextStatus == OrderStatus.CANCELLED) return getMessage.apply("Order cancelled");
            if(nextStatus == OrderStatus.INVOICE_ISSUED) return getMessage.apply("Invoice generated");
            if(nextStatus == OrderStatus.RETURN_REQUESTED) return getMessage.apply("Return initiated");
            if(nextStatus == OrderStatus.REFUND_PENDING) return getMessage.apply("Refund initiated");
        }

        if(currentStatus == OrderStatus.REFUND_PENDING && nextStatus == OrderStatus.REFUND_COMPLETED) return getMessage.apply("Refund completed");
        if(currentStatus == OrderStatus.RETURN_REQUESTED && nextStatus == OrderStatus.RETURN_RECEIVED) return getMessage.apply("Return received");

        return "";
    }
}
