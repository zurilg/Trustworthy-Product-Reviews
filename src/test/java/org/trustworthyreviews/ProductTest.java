package org.trustworthyreviews;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

public class ProductTest {
    @org.junit.Test
    public void testProductCreation() {
        Instant createdAt = Instant.now();
        Product p = new Product("Product", "http://example.com/product", "http://example.com/pic.jpg", "Category", createdAt);

        assertEquals("Product", p.getName());
        assertEquals("http://example.com/product", p.getCanonicalURL());
        assertEquals("http://example.com/pic.jpg", p.getPictureURL());
        assertEquals("Category", p.getCategory());
        assertEquals(createdAt, p.getCreatedAt());
    }

    @org.junit.Test
    public void testProductSetters() {
        Instant createdAt = Instant.now();
        Product p = new Product("Product", "http://example.com/product", "http://example.com/pic.jpg", "Category", createdAt);

        UUID id = UUID.randomUUID();
        p.setId(id);
        p.setName("NewProduct");
        p.setCanonicalURL("http://example.com/newproduct");
        p.setPictureURL("http://example.com/newpic.jpg");
        p.setCategory("NewCategory");

        // Create users and reviews to set
        User user1 = new User("UserName1", "DisplayName1", "email1@email.com");
        User user2 = new User("UserName2", "DisplayName2", "email2@email.com");
        List<Review> reviews = List.of(new Review(p, user1, Instant.now(), 5, "Nice product."),
                new Review(p, user2, Instant.now(), 1, "This product stinks!"));

        p.setReviews(reviews); // Set reviews

        // Verify all attributes
        assertEquals(id, p.getId());
        assertEquals("NewProduct", p.getName());
        assertEquals("http://example.com/newproduct", p.getCanonicalURL());
        assertEquals("http://example.com/newpic.jpg", p.getPictureURL());
        assertEquals("NewCategory", p.getCategory());

        for(int i = 0; i < reviews.size(); i++) {
            assert p.getReviews().get(i).getProduct().equals(p);
            assert p.getReviews().get(i).getAuthor().equals(reviews.get(i).getAuthor());
            assert p.getReviews().get(i).getRating() == reviews.get(i).getRating();
            assert p.getReviews().get(i).getContent().equals(reviews.get(i).getContent());
            assert p.getReviews().get(i).getCreatedAt().equals(reviews.get(i).getCreatedAt());
        }
    }

    @org.junit.Test
    public void testProductRatings(){
        // Create product
        Product p = new Product(UUID.randomUUID(),"Product", "http://example.com/product", "http://example.com/pic.jpg", "Category", Instant.now());

        // Create user and reviews
        int[] ratings = {5, 1, 3, 4, 2};
        User user = new User("UserName1", "DisplayName1", "email1@email.com");
        List<Review> reviews = List.of(new Review(UUID.randomUUID(), p, user, Instant.now(), ratings[0], "Nice product."),
                new Review(UUID.randomUUID(), p, user, Instant.now(), ratings[1], "This product stinks!"),
                new Review(UUID.randomUUID(), p, user, Instant.now(), ratings[2], "It's ok."),
                new Review(UUID.randomUUID(), p, user, Instant.now(), ratings[3], "Pretty good."),
                new Review(UUID.randomUUID(), p, user, Instant.now(), ratings[4], "Not great."));


        p.addReview(reviews.get(0));                                   // Add first review
        assertEquals(1, p.reviewsCount());               // Verify one review added
        assertEquals(ratings[0], p.avgRating(), 0.001);          // Verify rating

        p.addReview(reviews.get(1));                                   // Add second review
        assertEquals(2, p.reviewsCount());               // Verify two reviews added
        assertEquals((double) (ratings[0] + ratings[1]) / 2, p.avgRating(), 0.001);     // Verify rating

        p.addReview(reviews.get(2));                                   // Add third review
        assertEquals(3, p.reviewsCount());               // Verify three reviews added
        assertEquals((double) (ratings[0] + ratings[1] + ratings[2]) / 3, p.avgRating(), 0.001); // Verify rating

        p.addReview(reviews.get(3));                                   // Add fourth review
        assertEquals(4, p.reviewsCount());               // Verify four reviews added
        assertEquals((double) (ratings[0] + ratings[1] + ratings[2] + ratings[3]) / 4, p.avgRating(), 0.001); // Verify rating

        p.addReview(reviews.get(4));                                   // Add fifth review
        assertEquals(5, p.reviewsCount());               // Verify five reviews added
        assertEquals((double) (ratings[0] + ratings[1] + ratings[2] + ratings[3] + ratings[4]) / 5, p.avgRating(), 0.001); // Verify rating

        // Now remove reviews one by one and verify
        p.removeReview(reviews.get(0));
        assertEquals(4, p.reviewsCount());
        assertTrue(checkReviewRemoved(p, reviews.get(0)));

        p.removeReview(reviews.get(1));
        assertEquals(3, p.reviewsCount());
        assertTrue(checkReviewRemoved(p, reviews.get(1)));

        p.removeReview(reviews.get(2));
        assertEquals(2, p.reviewsCount());
        assertTrue(checkReviewRemoved(p, reviews.get(2)));

        p.removeReview(reviews.get(3));
        assertEquals(1, p.reviewsCount());
        assertTrue(checkReviewRemoved(p, reviews.get(3)));

        p.removeReview(reviews.get(4));
        assertEquals(0, p.reviewsCount());
        assertTrue(checkReviewRemoved(p, reviews.get(4)));
    }

    // Helper method to check for removed review
    public boolean checkReviewRemoved(Product p, Review r) {
        for(Review review : p.getReviews()) {
            if(review.getId().equals(r.getId())) {
                return false; // Review still exists
            }
        }
        return true; // Review successfully removed
    }

}
