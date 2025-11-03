package org.trustworthyreviews.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.trustworthyreviews.Review;

import java.util.UUID;

/**
 * The ReviewInterface defines the contract for review-related operations.
 *
 * @version 11-03-2025
 */
public interface ReviewInterface {
    /**
     * Create a new review for a product by an author.
     *
     * @param productId The ID of the product to review
     * @param authorId  The ID of the author creating the review
     * @return The created Review object
     */
    Review create(UUID productId, UUID authorId);

    /**
     * List reviews for a specific product with pagination.
     *
     * @param productId The ID of the product
     * @param pageable  The pagination information
     * @return A page of reviews for the specified product
     */
    Page<Review> listForProduct(UUID productId, Pageable pageable);

    /**
     * Count the number of reviews for a specific product.
     *
     * @param productId The ID of the product
     * @return The number of reviews for the specified product
     */
    long countForProduct(UUID productId);
}
