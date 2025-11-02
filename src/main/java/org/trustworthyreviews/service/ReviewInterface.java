package org.trustworthyreviews.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.trustworthyreviews.Review;

import java.util.UUID;

public interface ReviewInterface {
    Review create(UUID productId, UUID authorId);
    Page<Review> listForProduct(UUID productId, Pageable pageable);
    long countForProduct(UUID productId);
}
