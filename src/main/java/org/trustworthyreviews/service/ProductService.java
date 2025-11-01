package org.trustworthyreviews.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trustworthyreviews.Product;
import org.trustworthyreviews.repository.ProductRepository;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class ProductService implements ProductInterface {

    private final ProductRepository products;

    public ProductService(ProductRepository products) {
        this.products = products;
    }

    @Override
    public Page<Product> list(Pageable pageable) {
        return products.findAll(pageable);
    }

    @Override
    public Optional<Product> get(UUID id) {
        return products.findById(id);
    }
}
