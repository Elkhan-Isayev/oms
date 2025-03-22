package com.oms.inventory.repository;

import com.oms.inventory.model.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ProductRepository {
    
    private final AtomicLong idCounter = new AtomicLong(1);
    private final List<Product> products = new ArrayList<>();

    public Product save(Product product) {
        if (product.getId() == null) {
            product.setId(idCounter.getAndIncrement());
        } else {
            products.removeIf(existingProduct -> existingProduct.getId().equals(product.getId()));
        }
        products.add(product);
        return product;
    }

    public Optional<Product> findById(Long id) {
        return products.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst();
    }

    public List<Product> findAll() {
        return new ArrayList<>(products);
    }

    public void deleteById(Long id) {
        products.removeIf(product -> product.getId().equals(id));
    }
}
