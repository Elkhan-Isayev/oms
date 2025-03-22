package com.oms.inventory.repository;

import com.oms.inventory.model.Stock;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class StockRepository {
    
    private final AtomicLong idCounter = new AtomicLong(1);
    private final List<Stock> stocks = new ArrayList<>();

    public Stock save(Stock stock) {
        if (stock.getId() == null) {
            stock.setId(idCounter.getAndIncrement());
        } else {
            stocks.removeIf(existingStock -> existingStock.getId().equals(stock.getId()));
        }
        stocks.add(stock);
        return stock;
    }

    public Optional<Stock> findById(Long id) {
        return stocks.stream()
                .filter(stock -> stock.getId().equals(id))
                .findFirst();
    }

    public List<Stock> findByProductId(Long productId) {
        return stocks.stream()
                .filter(stock -> stock.getProductId().equals(productId))
                .collect(Collectors.toList());
    }

    public List<Stock> findByWarehouseId(Long warehouseId) {
        return stocks.stream()
                .filter(stock -> stock.getWarehouseId().equals(warehouseId))
                .collect(Collectors.toList());
    }

    public Optional<Stock> findByProductIdAndWarehouseId(Long productId, Long warehouseId) {
        return stocks.stream()
                .filter(stock -> stock.getProductId().equals(productId) && stock.getWarehouseId().equals(warehouseId))
                .findFirst();
    }

    public List<Stock> findAll() {
        return new ArrayList<>(stocks);
    }

    public void deleteById(Long id) {
        stocks.removeIf(stock -> stock.getId().equals(id));
    }
}
