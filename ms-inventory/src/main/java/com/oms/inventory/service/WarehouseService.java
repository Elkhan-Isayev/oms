package com.oms.inventory.service;

import com.oms.inventory.dto.WarehouseDto;
import com.oms.inventory.model.Warehouse;
import com.oms.inventory.repository.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;

    @Autowired
    public WarehouseService(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    public Warehouse createWarehouse(WarehouseDto warehouseDto) {
        Warehouse warehouse = new Warehouse();
        warehouse.setName(warehouseDto.getName());
        warehouse.setLocation(warehouseDto.getLocation());
        
        return warehouseRepository.save(warehouse);
    }

    public List<Warehouse> getAllWarehouses() {
        return warehouseRepository.findAll();
    }

    public Warehouse getWarehouseById(Long id) {
        return warehouseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Warehouse not found with id: " + id));
    }

    public Warehouse updateWarehouse(Long id, WarehouseDto warehouseDto) {
        Warehouse warehouse = getWarehouseById(id);
        
        warehouse.setName(warehouseDto.getName());
        warehouse.setLocation(warehouseDto.getLocation());
        warehouse.setUpdatedAt(LocalDateTime.now());
        
        return warehouseRepository.save(warehouse);
    }

    public void deleteWarehouse(Long id) {
        warehouseRepository.deleteById(id);
    }
}
