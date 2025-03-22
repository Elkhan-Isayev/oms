package com.oms.order.repository;

import com.oms.order.model.Order;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class OrderRepository {
    
    private final AtomicLong idCounter = new AtomicLong(1);
    private final List<Order> orders = new ArrayList<>();

    public Order save(Order order) {
        if (order.getId() == null) {
            order.setId(idCounter.getAndIncrement());
        } else {
            orders.removeIf(existingOrder -> existingOrder.getId().equals(order.getId()));
        }
        orders.add(order);
        return order;
    }

    public Optional<Order> findById(Long id) {
        return orders.stream()
                .filter(order -> order.getId().equals(id))
                .findFirst();
    }

    public List<Order> findByCustomerId(Long customerId) {
        return orders.stream()
                .filter(order -> order.getCustomerId().equals(customerId))
                .collect(Collectors.toList());
    }

    public List<Order> findAll() {
        return new ArrayList<>(orders);
    }

    public void deleteById(Long id) {
        orders.removeIf(order -> order.getId().equals(id));
    }
}
