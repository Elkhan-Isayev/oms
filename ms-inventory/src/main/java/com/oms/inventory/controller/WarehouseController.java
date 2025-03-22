package com.oms.inventory.controller;

import com.oms.inventory.dto.WarehouseDto;
import com.oms.inventory.model.Warehouse;
import com.oms.inventory.service.WarehouseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/warehouses")
@Tag(name = "Warehouse API", description = "Endpoints for warehouse management")
@SecurityRequirement(name = "bearerAuth")
public class WarehouseController {

    private final WarehouseService warehouseService;

    @Autowired
    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @PostMapping
    @Operation(summary = "Create a new warehouse")
    public ResponseEntity<Warehouse> createWarehouse(@RequestBody WarehouseDto warehouseDto,
                                                  @RequestHeader("X-User-Role") String role) {
        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        Warehouse newWarehouse = warehouseService.createWarehouse(warehouseDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newWarehouse);
    }

    @GetMapping
    @Operation(summary = "Get all warehouses")
    public ResponseEntity<List<Warehouse>> getAllWarehouses() {
        List<Warehouse> warehouses = warehouseService.getAllWarehouses();
        return ResponseEntity.ok(warehouses);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get warehouse by ID")
    public ResponseEntity<Warehouse> getWarehouseById(@PathVariable Long id) {
        Warehouse warehouse = warehouseService.getWarehouseById(id);
        return ResponseEntity.ok(warehouse);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a warehouse")
    public ResponseEntity<Warehouse> updateWarehouse(@PathVariable Long id, 
                                                  @RequestBody WarehouseDto warehouseDto,
                                                  @RequestHeader("X-User-Role") String role) {
        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        Warehouse updatedWarehouse = warehouseService.updateWarehouse(id, warehouseDto);
        return ResponseEntity.ok(updatedWarehouse);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a warehouse")
    public ResponseEntity<Void> deleteWarehouse(@PathVariable Long id,
                                             @RequestHeader("X-User-Role") String role) {
        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        warehouseService.deleteWarehouse(id);
        return ResponseEntity.noContent().build();
    }
}
