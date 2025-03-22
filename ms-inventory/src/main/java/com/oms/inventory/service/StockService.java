package com.oms.inventory.service;

import com.oms.inventory.dto.StockDto;
import com.oms.inventory.model.Product;
import com.oms.inventory.model.Stock;
import com.oms.inventory.model.Warehouse;
import com.oms.inventory.repository.StockRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class StockService {

    private static final Logger logger = LoggerFactory.getLogger(StockService.class);
    
    private final StockRepository stockRepository;
    private final ProductService productService;
    private final WarehouseService warehouseService;
    
    @Value("${inventory.lowstock.threshold:5}")
    private int lowStockThreshold;

    @Autowired
    public StockService(StockRepository stockRepository, 
                      ProductService productService, 
                      WarehouseService warehouseService) {
        this.stockRepository = stockRepository;
        this.productService = productService;
        this.warehouseService = warehouseService;
    }

    public Stock addStock(StockDto stockDto) {
        // Validate product and warehouse exist
        Product product = productService.getProductById(stockDto.getProductId());
        Warehouse warehouse = warehouseService.getWarehouseById(stockDto.getWarehouseId());
        
        // Check if stock record already exists for this product-warehouse combination
        Optional<Stock> existingStock = stockRepository.findByProductIdAndWarehouseId(
                stockDto.getProductId(), stockDto.getWarehouseId());
        
        if (existingStock.isPresent()) {
            // Update existing stock
            Stock stock = existingStock.get();
            stock.setQuantityAvailable(stock.getQuantityAvailable() + stockDto.getQuantityAvailable());
            stock.setUpdatedAt(LocalDateTime.now());
            return stockRepository.save(stock);
        } else {
            // Create new stock record
            Stock stock = new Stock();
            stock.setProductId(stockDto.getProductId());
            stock.setWarehouseId(stockDto.getWarehouseId());
            stock.setQuantityAvailable(stockDto.getQuantityAvailable());
            return stockRepository.save(stock);
        }
    }

    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    public List<Stock> getStocksByProductId(Long productId) {
        return stockRepository.findByProductId(productId);
    }

    public List<Stock> getStocksByWarehouseId(Long warehouseId) {
        return stockRepository.findByWarehouseId(warehouseId);
    }

    public Stock updateStockQuantity(Long id, Integer quantity) {
        Stock stock = stockRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Stock not found with id: " + id));
        
        stock.setQuantityAvailable(quantity);
        stock.setUpdatedAt(LocalDateTime.now());
        
        Stock updatedStock = stockRepository.save(stock);
        
        // Check if stock is low
        if (updatedStock.getQuantityAvailable() <= lowStockThreshold) {
            Product product = productService.getProductById(updatedStock.getProductId());
            Warehouse warehouse = warehouseService.getWarehouseById(updatedStock.getWarehouseId());
            
            logger.warn("Low stock alert: Product '{}' has only {} units available in warehouse '{}'",
                    product.getName(), updatedStock.getQuantityAvailable(), warehouse.getName());
        }
        
        return updatedStock;
    }

    public boolean reserveStock(Long productId, Long quantity) {
        // Find stocks for this product
        List<Stock> productStocks = stockRepository.findByProductId(productId);
        
        if (productStocks.isEmpty()) {
            logger.warn("No stock found for product ID: {}", productId);
            return false;
        }
        
        // Find a warehouse with enough stock
        Optional<Stock> availableStock = productStocks.stream()
                .filter(s -> s.getQuantityAvailable() >= quantity)
                .findFirst();
        
        if (availableStock.isEmpty()) {
            logger.warn("Insufficient stock for product ID: {}", productId);
            return false;
        }
        
        // Reserve the stock
        Stock stock = availableStock.get();
        stock.setQuantityAvailable(stock.getQuantityAvailable() - quantity.intValue());
        stock.setUpdatedAt(LocalDateTime.now());
        stockRepository.save(stock);
        
        // Check if stock is low after reservation
        if (stock.getQuantityAvailable() <= lowStockThreshold) {
            Product product = productService.getProductById(stock.getProductId());
            Warehouse warehouse = warehouseService.getWarehouseById(stock.getWarehouseId());
            
            logger.warn("Low stock alert after reservation: Product '{}' has only {} units available in warehouse '{}'",
                    product.getName(), stock.getQuantityAvailable(), warehouse.getName());
        }
        
        return true;
    }

    public void releaseStock(Long productId, Long quantity) {
        // Find stocks for this product
        List<Stock> productStocks = stockRepository.findByProductId(productId);
        
        if (productStocks.isEmpty()) {
            logger.warn("No stock found for product ID: {} to release", productId);
            return;
        }
        
        // Release to the first warehouse in the list (or implement a more sophisticated strategy)
        Stock stock = productStocks.get(0);
        stock.setQuantityAvailable(stock.getQuantityAvailable() + quantity.intValue());
        stock.setUpdatedAt(LocalDateTime.now());
        stockRepository.save(stock);
        
        logger.info("Released {} units of product ID: {} back to stock", quantity, productId);
    }
}
