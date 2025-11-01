package org.trustworthyreviews.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.trustworthyreviews.model.ReviewModel;

import java.util.UUID;

/**
 * ReviewService (interface)
 * Operations the web layer needs (create review, list by product, count by product).
 * Returns ReviewModel (DTO), not entities.
 * Implemented in ReviewServiceImpl. Controllers depend on this interface.
 */
public interface ReviewService {
    ReviewModel create(UUID productId, UUID authorId, int rating);

    /**
     * NOTE: Pageable may be null (e.g., UI didn't supply page/size). Implementation
     * should handle null by applying a sane default (see ReviewServiceImpl).
     */
    Page<ReviewModel> listForProduct(UUID productId, Pageable pageable);

    long countForProduct(UUID productId);
}
