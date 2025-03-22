package com.oms.inventory.controller;

import com.oms.inventory.dto.ProductDto;
import com.oms.inventory.model.Product;
import com.oms.inventory.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Product API", description = "Endpoints for product management")
@SecurityRequirement(name = "bearerAuth")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @Operation(summary = "Create a new product")
    public ResponseEntity<Product> createProduct(@RequestBody ProductDto productDto, 
                                               @RequestHeader("X-User-Role") String role) {
        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        Product newProduct = productService.createProduct(productDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
    }

    @GetMapping
    @Operation(summary = "Get all products")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a product")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, 
                                               @RequestBody ProductDto productDto,
                                               @RequestHeader("X-User-Role") String role) {
        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        Product updatedProduct = productService.updateProduct(id, productDto);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a product")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id,
                                            @RequestHeader("X-User-Role") String role) {
        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
