package org.trustworthyreviews;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.trustworthyreviews.repository.ProductRepository;
import org.trustworthyreviews.repository.ReviewRepository;
import org.trustworthyreviews.repository.UserRepository;

import java.util.Random;

/**
 * The main application class for Trustworthy Product Reviews.
 *
 * @version 11-03-2025
 */
@SpringBootApplication
public class TrustworthyProductReviewsApplication {

    /**
     * The main method to start the Spring Boot application.
     *
     * @param args Unused.
     */
    public static void main(String[] args) {
        SpringApplication.run(TrustworthyProductReviewsApplication.class, args);
    }

    /**
     * Populates the repositories with sample data on application startup.
     *
     * @param productRepo The product repository
     * @param reviewRepo The review repository
     * @param userRepo The user repository
     * @return A CommandLineRunner to populate sample data
     */
    @Bean
    public CommandLineRunner demo(ProductRepository productRepo, ReviewRepository reviewRepo, UserRepository userRepo) {
        return (args) -> {
            // Populate user repo with user sample data
            int numberOfUsersToCreate = 10;
            for(int i = 0; i < numberOfUsersToCreate; i++) {
                User user = new User("user" + i, "User " + i, "email" + i + "@email.com");
                userRepo.save(user);
            }

            // Populate product repo with product sample data
            int numberOfProductsToCreate = 5;
            String[] products = {"Crate", "Shoes", "Casio Watch", "iPhone 12", "Lenovo Laptop"};
            String[] categories = {"Storage", "Footwear", "Electronics", "Electronics", "Electronics"};
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
                product.setCategory(categories[i]);
                product.setCanonicalURL(productPhotos[i]);
                product.setPictureURL(productPhotos[i]);
                product.setCreatedAt(java.time.Instant.now());
                productRepo.save(product);
            }

            // Populate review repo with review sample data
            int numberOfReviewsToCreate = 30;
            Random rand = new Random();
            for(int i = 0; i < numberOfReviewsToCreate; i++) {
                User user = userRepo.findById(userRepo.findAll().get(i % numberOfUsersToCreate).getId()).get();
                Product product = productRepo.findById(productRepo.findAll().get(i % numberOfProductsToCreate).getId()).get();
                Review review = new Review(product, user, java.time.Instant.now());
                review.setRating(rand.nextInt(5) + 1); // Ratings between 1 and 5
                review.setContent("This is review " + i + " for " + product.getName());
                reviewRepo.save(review);
            }
        };
    }
}
