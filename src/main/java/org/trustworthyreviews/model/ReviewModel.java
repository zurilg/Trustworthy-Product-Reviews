package org.trustworthyreviews.model;

import java.time.Instant;
import java.util.UUID;

/**
 * ReviewModel = "DTO"/"View Model" used by controllers and views.
 * A minimal, immutable snapshot of a review the UI/REST needs to display.
 * Only IDs of related entities (productId, authorId), not the entities themselves.
 * Constructed in the service by mapping from a Review entity (see toModel()).
 */
public class ReviewModel {
    private final UUID id;
    private final UUID productId;
    private final UUID authorId;
    private final int rating;        // 1..5
    private final Instant createdAt; // server timestamp

    public ReviewModel(UUID id, UUID productId, UUID authorId, int rating, Instant createdAt) {
        this.id = id;
        this.productId = productId;
        this.authorId = authorId;
        this.rating = rating;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public UUID getProductId() {
        return productId;
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public int getRating() {
        return rating;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
