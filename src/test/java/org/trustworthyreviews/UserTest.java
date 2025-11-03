package org.trustworthyreviews;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import static org.junit.Assert.assertEquals;

/**
 * Test class for User.
 *
 * @version 11-03-2025
 */
public class UserTest {
    /**
     * Method to test user creation.
     */
    @org.junit.Test
    public void testUserCreation() {
        // Test code for user creation
        User user = new User("UserName", "DisplayName", "email@email.com");
        assertEquals("UserName", user.getUserName());
        assertEquals("DisplayName", user.getDisplayName());
        assertEquals("email@email.com", user.getEmail());
    }

    /**
     * Method to test user setters.
     */
    @org.junit.Test
    public void testUserSetters() {
        // Create a user
        User user = new User("UserName", "DisplayName", "email@email.com");

        // Change all initialized attributes using setters
        UUID id = UUID.randomUUID();
        user.setId(id);
        user.setUserName("newUserName");
        user.setDisplayName("newDisplayName");
        user.setEmail("newEmail@email.com");

        // Create some products and reviews to set
        Product p1 = new Product(UUID.randomUUID(), "Product1", "http://example.com/product1", "http://example.com/pic1.jpg", "Category1", Instant.now());
        Product p2 = new Product(UUID.randomUUID(), "Product2", "http://example.com/product2", "http://example.com/pic2.jpg", "Category2", Instant.now());
        List<Review> reviews = List.of(new Review(p1, user, Instant.now(), 5, "Nice product."),
                new Review(p2, user, Instant.now(), 1, "This product stinks!"));

        // Set reviews
        user.setReviews(reviews);

        // Verify all attributes
        assertEquals(id, user.getId());
        assertEquals("newUserName", user.getUserName());
        assertEquals("newDisplayName", user.getDisplayName());
        assertEquals("newEmail@email.com", user.getEmail());

        assertEquals(user.getReviews().size(), reviews.size());
        for (int i = 0; i < reviews.size(); i++) {
            assertEquals(user, user.getReviews().get(i).getAuthor());
            assertEquals("Product" + (i + 1), user.getReviews().get(i).getProduct().getName());
            assertEquals("http://example.com/product" + (i + 1), user.getReviews().get(i).getProduct().getCanonicalURL());
            assertEquals("http://example.com/pic" + (i + 1) + ".jpg", user.getReviews().get(i).getProduct().getPictureURL());
            assertEquals("Category" + (i + 1), user.getReviews().get(i).getProduct().getCategory());
            assertEquals(reviews.get(i).getRating(), user.getReviews().get(i).getRating());
            assertEquals(reviews.get(i).getContent(), user.getReviews().get(i).getContent());
            assertEquals(reviews.get(i).getCreatedAt(), user.getReviews().get(i).getCreatedAt());
        }
    }
}
