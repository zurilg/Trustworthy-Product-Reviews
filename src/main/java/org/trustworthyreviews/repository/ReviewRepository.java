package org.trustworthyreviews.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.trustworthyreviews.Review;

import java.util.List;
import java.util.UUID;

/**
 * The ReviewRepository interface for managing Review entities.
 *
 * @version 11-03-2025
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID>, JpaSpecificationExecutor<Review> {
    /**
     * Find reviews by product ID ordered by creation date descending.
     *
     * @param productId The ID of the product
     * @return A list of reviews for the specified product
     */
    List<Review> findByProductIdOrderByCreatedAtDesc(UUID productId);

    /**
     * Find reviews by product ID with pagination.
     *
     * @param productId The ID of the product
     * @param pageable  The pagination information
     * @return A page of reviews for the specified product
     */
    Page<Review> findByProductId(UUID productId, Pageable pageable);

    /**
     * Count the number of reviews for a specific product.
     *
     * @param productId The ID of the product
     * @return The number of reviews for the specified product
     */
    long countByProductId(UUID productId);

    /**
     * Find reviews by author ID.
     *
     * @param authorId The ID of the author
     * @return A list of reviews written by the specified author
     */
    List<Review> findByAuthorId(UUID authorId);
}