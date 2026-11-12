package com.tecsup.minishop.service;

import com.tecsup.minishop.model.Product;
import com.tecsup.minishop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository repository;

    public Product save(Product product) {
        if (product.getPrice() == null || !Double.isFinite(product.getPrice()) || product.getPrice() <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a cero");
        }
        if (product.getStock() == null || product.getStock() < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
        return repository.save(product);
    }

    public List<Product> findAll() {
        return repository.findAll();
    }

    public Product findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
    }
}
