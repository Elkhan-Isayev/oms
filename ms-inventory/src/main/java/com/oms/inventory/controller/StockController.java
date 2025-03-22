package com.oms.inventory.controller;

import com.oms.inventory.dto.StockDto;
import com.oms.inventory.model.Stock;
import com.oms.inventory.service.StockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
@Tag(name = "Stock API", description = "Endpoints for stock management")
@SecurityRequirement(name = "bearerAuth")
public class StockController {

    private final StockService stockService;

    @Autowired
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @PostMapping
    @Operation(summary = "Add stock record")
    public ResponseEntity<Stock> addStock(@RequestBody StockDto stockDto,
                                        @RequestHeader("X-User-Role") String role) {
        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        Stock newStock = stockService.addStock(stockDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newStock);
    }

    @GetMapping
    @Operation(summary = "Get all stock records")
    public ResponseEntity<List<Stock>> getAllStocks() {
        List<Stock> stocks = stockService.getAllStocks();
        return ResponseEntity.ok(stocks);
    }

    @GetMapping("/product/{productId}")
    @Operation(summary = "Get stock by product ID")
    public ResponseEntity<List<Stock>> getStocksByProductId(@PathVariable Long productId) {
        List<Stock> stocks = stockService.getStocksByProductId(productId);
        return ResponseEntity.ok(stocks);
    }

    @GetMapping("/warehouse/{warehouseId}")
    @Operation(summary = "Get stock by warehouse ID")
    public ResponseEntity<List<Stock>> getStocksByWarehouseId(@PathVariable Long warehouseId) {
        List<Stock> stocks = stockService.getStocksByWarehouseId(warehouseId);
        return ResponseEntity.ok(stocks);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update stock quantity")
    public ResponseEntity<Stock> updateStockQuantity(@PathVariable Long id, 
                                                  @RequestParam Integer quantity,
                                                  @RequestHeader("X-User-Role") String role) {
        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        Stock updatedStock = stockService.updateStockQuantity(id, quantity);
        return ResponseEntity.ok(updatedStock);
    }

    @PostMapping("/reserve")
    @Operation(summary = "Reserve stock for an order", description = "Decrements stock quantity for the specified product")
    public ResponseEntity<Boolean> reserveStock(@RequestParam Long productId, @RequestParam Long quantity) {
        boolean success = stockService.reserveStock(productId, quantity);
        return ResponseEntity.ok(success);
    }

    @PostMapping("/release")
    @Operation(summary = "Release reserved stock", description = "Increments stock quantity for the specified product")
    public ResponseEntity<Void> releaseStock(@RequestParam Long productId, @RequestParam Long quantity) {
        stockService.releaseStock(productId, quantity);
        return ResponseEntity.ok().build();
    }
}
