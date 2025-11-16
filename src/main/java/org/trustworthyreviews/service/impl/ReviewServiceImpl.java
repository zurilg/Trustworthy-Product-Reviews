package org.trustworthyreviews.service.impl;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trustworthyreviews.Product;
import org.trustworthyreviews.Review;
import org.trustworthyreviews.User;
import org.trustworthyreviews.model.ReviewModel;
import org.trustworthyreviews.repository.ProductRepository;
import org.trustworthyreviews.repository.ReviewRepository;
import org.trustworthyreviews.repository.UserRepository;
import org.trustworthyreviews.service.ReviewService;

import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.UUID;

/**
 * ReviewServiceImpl (concrete @Service)
 * Encapsulates business logic so controllers stay thin.
 * Prevents controllers from touching repositories directly for non-trivial flows.
 * Ensures transactions are applied at the correct boundary.
 * Validates input (rating range), checks existence of product/user,
 * creates Review, and maps entity -> ReviewModel for the web layer.
 * Provides listing + counting by product using existing repository methods
 * DI via constructor.
 * @Transactional on write; readOnly on read paths.
 * Handles null Pageable by applying a default (page 0, size 10, newest first).
 *
 * @version 11-03-2025
 */
@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviews;
    private final ProductRepository products;
    private final UserRepository users;

    /**
     * Constructor for ReviewServiceImpl with dependency injection.
     *
     * @param reviews  The ReviewRepository
     * @param products The ProductRepository
     * @param users    The UserRepository
     */
    public ReviewServiceImpl(ReviewRepository reviews,
                             ProductRepository products,
                             UserRepository users) {
        this.reviews = reviews;
        this.products = products;
        this.users = users;
    }

    /**
     * Create a review for a given product + author with a rating.
     * Returns a DTO (ReviewModel) instead of the JPA entity.
     */
    @Override
    @Transactional
    public ReviewModel create(UUID productId, UUID authorId, int rating, String content) {
        // Basic invariant: rating must be 1..5
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("rating must be between 1 and 5");
        }

        // Look up required entities. If not found, fail fast with clear messages.
        Product product = products.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("product not found"));
        User author = users.findById(authorId)
                .orElseThrow(() -> new NoSuchElementException("author not found"));

        // Build and persist entity
        Review r = new Review();
        r.setProduct(product);
        r.setAuthor(author);
        r.setRating(rating);
        r.setContent(content);
        r.setCreatedAt(Instant.now());
        Review saved = reviews.save(r);

        // Return the DTO
        return toModel(saved);
    }

    /**
     * List reviews for a product with optional pagination.
     * If pageable is null, we apply a sensible default (page 0, size 10, newest first).
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ReviewModel> listForProduct(UUID productId, Pageable pageable) {
        // Null-safe pagination
        if (pageable == null) {
            pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        }

        Page<Review> page = reviews.findByProductId(productId, pageable);

        // Map Page<Review> -> Page<ReviewModel> (functional map keeps pagination metadata).
        return page.map(this::toModel);
    }

    /**
     * Count reviews for a product.
     */
    @Override
    @Transactional(readOnly = true)
    public long countForProduct(UUID productId) {
        return reviews.countByProductId(productId);
    }

    /**
     * Local mapper from entity -> DTO.
     */
    private ReviewModel toModel(Review r) {
        return new ReviewModel(
                r.getId(),
                r.getProduct() != null ? r.getProduct().getId() : null,
                r.getAuthor() != null ? r.getAuthor().getId() : null,
                r.getRating(),
                r.getContent(),
                r.getCreatedAt()
        );
    }
}