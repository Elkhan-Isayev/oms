package com.oms.order.model;

/**
 * Enum representing the various states an order can be in.
 */
public enum OrderStatus {
    CREATED,        // Initial state when an order is created
    CONFIRMED,      // Order has been confirmed by the system
    PAID,           // Payment for the order has been received
    PROCESSING,     // Order is being processed for fulfillment
    SHIPPED,        // Order has been shipped
    DELIVERED,      // Order has been delivered to the customer
    CANCELLED,      // Order has been cancelled
    RETURNED        // Order has been returned by the customer
}