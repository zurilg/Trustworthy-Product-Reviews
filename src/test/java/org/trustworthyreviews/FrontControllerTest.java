package org.trustworthyreviews;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.trustworthyreviews.repository.CategoryRepository;
import org.trustworthyreviews.repository.ProductRepository;
import org.trustworthyreviews.repository.ReviewRepository;
import org.trustworthyreviews.repository.UserRepository;

import java.util.ArrayList;
import java.util.Random;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Tests the FrontController to ensure it is loading and serving expected content.
 * Integration test that uses TestRestTemplate to make HTTP requests to the application.
 *
 * @version 11-03-2025
 */
@SpringBootTest (webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FrontControllerTest {
    @Autowired
    private FrontController controller;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void contextLoads() throws Exception {
        assertThat(controller).isNotNull();
    }

    /**
     * Clears repositories and sets up test data before each test.
     *
     * @throws Exception Not expected.
     */
    @BeforeEach
    void setUp() throws Exception {
        // Clear repositories before tests.
        reviewRepository.deleteAll();
        productRepository.deleteAll();
        userRepository.deleteAll();

        // Add test data to repositories.
        // Populate user repo with user sample data
        int numberOfUsersToCreate = 10;
        for(int i = 0; i < numberOfUsersToCreate; i++) {
            User user = new User("user" + i, "User " + i, "email" + i + "@email.com");
            userRepository.save(user);
        }

        // Populate product repo with product sample data
        int numberOfProductsToCreate = 5;
        String[] products = {"Crate", "Shoes", "Casio Watch", "iPhone 12", "Lenovo Laptop"};
        String[] categoryNames = {"Storage", "Footwear", "Electronics", "Electronics", "Electronics"};
        ArrayList<Category> categories = new ArrayList<>();
        for(String categoryName : categoryNames) {
            Category category = new Category(categoryName);
            categoryRepository.save(category);
            categories.add(category);
        }
        String[] productPhotos = {
                "https://m.media-amazon.com/images/I/81NKzEV0+2L.jpg",
                "https://m.media-amazon.com/images/I/713tl8NjiaL._AC_SY575_.jpg",
                "https://m.media-amazon.com/images/I/61Xm9WEMuEL._AC_SX679_.jpg",
                "https://m.media-amazon.com/images/I/41WsaMyJAqL._AC_SY300_SX300_QL70_ML2_.jpg",
                "https://m.media-amazon.com/images/I/61v6QV8AQPL._AC_SX425_.jpg"
        };

        for(int i = 0; i < numberOfProductsToCreate; i++) {
            Product product = new Product();
            product.setName(products[i]);
            product.setCategory(categories.get(i));
            product.setCanonicalURL(productPhotos[i]);
            product.setPictureURL(productPhotos[i]);
            product.setCreatedAt(java.time.Instant.now());
            productRepository.save(product);
        }

        // Populate review repo with review sample data
        int numberOfReviewsToCreate = 30;
        Random rand = new Random();
        for(int i = 0; i < numberOfReviewsToCreate; i++) {
            User user = userRepository.findById(userRepository.findAll().get(i % numberOfUsersToCreate).getId()).get();
            Product product = productRepository.findById(productRepository.findAll().get(i % numberOfProductsToCreate).getId()).get();
            Review review = new Review(product, user, java.time.Instant.now());
            review.setRating(rand.nextInt(5) + 1); // Ratings between 1 and 5
            review.setContent("This is review " + i + " for " + product.getName());
            reviewRepository.save(review);
        }
    }

    /**
     * Checks that the content served by the front controller contains expected data for the home page
     * and all product pages.
     */
    @Test
    void checkContent(){
        // Test for data only contained on the home page.
        assertThat(this.restTemplate.getForObject("http://localhost:" +
                port + "/", String.class)).contains("Trustworthy Product Reviews");
        assertThat(this.restTemplate.getForObject("http://localhost:" +
                port + "/", String.class)).contains("Crate", "Casio Watch", "iPhone 12", "Lenovo Laptop", "Shoes");

        // Test for data only contained in product dedicated pages.
        String[] products = {"Crate", "Shoes", "Casio Watch", "iPhone 12", "Lenovo Laptop"};
        for(int i = 0; i<products.length; i++){
            // Test that the product's title is contained within the page.
            assertThat(this.restTemplate.getForObject("http://localhost:" +
                    port + "/product/" + productRepository.findAll().get(i).getId(), String.class)).contains(products[i]);

            // Test that a different product's title is not contained within the page.
            String notContainedProduct = (i - 1) < 0 ? products[i + 1] : products[i - 1];
            assertThat(this.restTemplate.getForObject("http://localhost:" +
                    port + "/product/" + productRepository.findAll().get(i).getId(), String.class)).doesNotContain(notContainedProduct);
        }
    }
}
