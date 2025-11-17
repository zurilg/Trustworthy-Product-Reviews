package org.trustworthyreviews.model;

import java.time.Instant;
import java.util.UUID;

/**
 * ReviewModel = "DTO"/"View Model" used by controllers and views.
 * A minimal, immutable snapshot of a review the UI/REST needs to display.
 * Only IDs of related entities (productId, authorId), not the entities themselves.
 * Constructed in the service by mapping from a Review entity (see toModel()).
 *
 * @version 11-17-2025
 */
public class ReviewModel {
    private final UUID id;
    private final UUID productId;
    private final UUID authorId;
    private final int rating;        // 1..5
    private final String content;
    private final Instant createdAt; // server timestamp

    /**
     * Constructor for ReviewModel
     *
     * @param id The unique identifier for the review
     * @param productId The ID of the product the review belongs to
     * @param authorId The ID of the author of the review
     * @param rating The rating given in the review
     * @param createdAt The creation timestamp of the review
     */
    public ReviewModel(UUID id, UUID productId, UUID authorId, int rating, String content, Instant createdAt) {
        this.id = id;
        this.productId = productId;
        this.authorId = authorId;
        this.rating = rating;
        this.content = content;
        this.createdAt = createdAt;
    }

    /**
     * Getters for ReviewModel fields
     */
    public UUID getId() {
        return id;
    }

    /**
     * Getter for productId
     *
     * @return The ID of the product the review belongs to
     */
    public UUID getProductId() {
        return productId;
    }

    /**
     * Getter for authorId
     *
     * @return The ID of the author of the review
     */
    public UUID getAuthorId() {
        return authorId;
    }

    /**
     * Getter for rating
     *
     * @return The rating given in the review
     */
    public int getRating() {
        return rating;
    }

    /**
     * Getter for content
     *
     * @return The content of the review
     */
    public String getContent() {
        return content;
    }

    /**
     * Getter for createdAt
     *
     * @return The creation timestamp of the review
     */
    public Instant getCreatedAt() {
        return createdAt;
    }
}