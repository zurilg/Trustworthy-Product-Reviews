package org.trustworthyreviews.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.trustworthyreviews.Product;

import java.util.Optional;
import java.util.UUID;

public interface ProductInterface {
    Page<Product> list(Pageable pageable);
    Optional<Product> get(UUID id);
}
