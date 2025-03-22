package com.oms.inventory.repository;

import com.oms.inventory.model.Warehouse;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class WarehouseRepository {
    
    private final AtomicLong idCounter = new AtomicLong(1);
    private final List<Warehouse> warehouses = new ArrayList<>();

    public Warehouse save(Warehouse warehouse) {
        if (warehouse.getId() == null) {
            warehouse.setId(idCounter.getAndIncrement());
        } else {
            warehouses.removeIf(existingWarehouse -> existingWarehouse.getId().equals(warehouse.getId()));
        }
        warehouses.add(warehouse);
        return warehouse;
    }

    public Optional<Warehouse> findById(Long id) {
        return warehouses.stream()
                .filter(warehouse -> warehouse.getId().equals(id))
                .findFirst();
    }

    public List<Warehouse> findAll() {
        return new ArrayList<>(warehouses);
    }

    public void deleteById(Long id) {
        warehouses.removeIf(warehouse -> warehouse.getId().equals(id));
    }
}
