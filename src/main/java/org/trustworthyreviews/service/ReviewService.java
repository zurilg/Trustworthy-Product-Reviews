package org.trustworthyreviews.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trustworthyreviews.Product;
import org.trustworthyreviews.Review;
import org.trustworthyreviews.User;
import org.trustworthyreviews.repository.ProductRepository;
import org.trustworthyreviews.repository.ReviewRepository;
import org.trustworthyreviews.repository.UserRepository;

import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class ReviewService implements ReviewInterface {

    private final ReviewRepository reviews;
    private final ProductRepository products;
    private final UserRepository users;

    public ReviewService(ReviewRepository reviews,
                             ProductRepository products,
                             UserRepository users) {
        this.reviews = reviews;
        this.products = products;
        this.users = users;
    }

    @Override
    @Transactional
    public Review create(UUID productId, UUID authorId) {
        if (productId == null || authorId == null) {
            throw new IllegalArgumentException("productId and authorId are required");
        }
        Product product = products.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("product not found"));
        User author = users.findById(authorId)
                .orElseThrow(() -> new NoSuchElementException("author not found"));

        Review review = new Review();
        review.setProduct(product);
        review.setAuthor(author);
        review.setCreatedAt(Instant.now()); // simple until @CreationTimestamp is added

        return reviews.save(review);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Review> listForProduct(UUID productId, Pageable pageable) {
        // Optional: assert product exists to return 404-style error earlier
        products.findById(productId).orElseThrow(() -> new NoSuchElementException("product not found"));
        // Recommended repository method name (nested path with underscore):
        return reviews.findByProductId(productId, pageable);
        // If you cannot rename, you can try: return reviews.findByProductId(productId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public long countForProduct(UUID productId) {
        return reviews.countByProductId(productId);
        // If you cannot rename, you can try: return reviews.countByProductId(productId);
    }
}
