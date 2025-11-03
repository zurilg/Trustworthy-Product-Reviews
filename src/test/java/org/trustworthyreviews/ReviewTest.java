package org.trustworthyreviews;

import java.time.Instant;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class ReviewTest {
    @org.junit.Test
    public void testReviewCreation() {
        // Create product and user for the review
        Product p1 = new Product(UUID.randomUUID(), "Product1", "http://example.com/product1", "http://example.com/pic1.jpg", "Category1", Instant.now());
        User user = new User("UserName", "DisplayName", "email@email.com");
        // Create review
        Instant now = Instant.now();
        Review review = new Review(p1, user, now, 4, "Great product!");

        // Verify review attributes
        assertEquals(p1, review.getProduct());
        assertEquals(user, review.getAuthor());
        assertEquals(now, review.getCreatedAt());
        assertEquals(4, review.getRating());
        assertEquals("Great product!", review.getContent());
    }

    @org.junit.Test
    public void testReviewSetters(){
        // Create products and users for the review setter testing
        Product p1 = new Product("Product1", "http://example.com/product1", "http://example.com/pic1.jpg", "Category1", Instant.now());
        Product p2 = new Product("Product2", "http://example.com/product2", "http://example.com/pic2.jpg", "Category2", Instant.now());

        User user1 = new User("UserName1", "DisplayName1", "email1@email.com");
        User user2 = new User("UserName2", "DisplayName2", "email2@email.com");

        // Create review
        Review review = new Review(p1, user1, Instant.now(), 3, "It's okay.");

        // Change all initialized attributes using setters
        UUID id = UUID.randomUUID();
        review.setId(id);
        review.setProduct(p2);
        review.setAuthor(user2);
        Instant newTime = Instant.now();
        review.setCreatedAt(newTime);
        review.setRating(5);
        review.setContent("Actually, it's great!");
        // Verify all attributes
        assertEquals(id, review.getId());
        assertEquals(p2, review.getProduct());
        assertEquals(user2, review.getAuthor());
        assertEquals(newTime, review.getCreatedAt());
        assertEquals(5, review.getRating());
        assertEquals("Actually, it's great!", review.getContent());
    }
}
