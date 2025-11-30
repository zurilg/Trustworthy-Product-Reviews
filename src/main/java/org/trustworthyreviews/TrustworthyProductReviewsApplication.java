package org.trustworthyreviews;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.trustworthyreviews.Follow;
import org.trustworthyreviews.repository.CategoryRepository;
import org.trustworthyreviews.repository.ProductRepository;
import org.trustworthyreviews.repository.ReviewRepository;
import org.trustworthyreviews.repository.UserRepository;
import org.trustworthyreviews.repository.FollowRepository;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.*;

/**
 * The main application class for Trustworthy Product Reviews.
 *
 * @version 11-03-2025
 */
@SpringBootApplication
@EnableCaching
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
    public CommandLineRunner demo(ProductRepository productRepo,
                                  ReviewRepository reviewRepo,
                                  UserRepository userRepo,
                                  CategoryRepository categoryRepo,
                                  FollowRepository followRepository) {
        return (args) -> {
            // Populate user repo with user sample data
            Map<String, User> usersByName = new HashMap<>();
            for(String[] userInfo: loadCsvData("users.csv")) {
                User user = new User(userInfo[0], userInfo[1], userInfo[2]);
                usersByName.put(userInfo[0], userRepo.save(user));
            }

            // Populate category repo with category sample data
            ArrayList<Category> categories = new ArrayList<>();
            for(String[] categoryName : loadCsvData("categories.csv")) {
                Category category = new Category(categoryName[0]);
                categories.add(category);
                categoryRepo.save(category);
            }

            // Populate product repo with product sample data
            for(String[] productInfo: loadCsvData("products.csv")) {
                Product product = new Product(productInfo[0], productInfo[1], productInfo[2],
                        categories.get(Integer.parseInt(productInfo[3])), Instant.now());
                productRepo.save(product);
            }

            // Populate review repo with review sample data
            for(String[] reviewInfo: loadCsvData("reviews.csv")) {
                Optional<Product> product = productRepo.findByName(reviewInfo[0]);
                Optional<User> author = userRepo.findByUserName(reviewInfo[1]);
                if(product.isEmpty() || author.isEmpty()) {
                    continue; // Skip invalid entries
                }
                Review review = new Review(product.get(), author.get(), Instant.now(), Integer.parseInt(reviewInfo[3]), reviewInfo[2]);
                reviewRepo.save(review);
            }

            seedFollows(usersByName, followRepository);
        };
    }

    /**
     * Loads CSV data from a file in the classpath.
     *
     * @param fileName The CSV file name to load data from
     * @return A list of string arrays representing the CSV data
     * @throws IOException If an I/O error occurs
     */
    private List<String[]> loadCsvData(String fileName) throws IOException {
        List<String[]> data = new ArrayList<>();
        ClassPathResource resource = new ClassPathResource("data/" + fileName);

        try (InputStream is = resource.getInputStream();
             Scanner scanner = new Scanner(is)) {

            // Skip the header row
            if (scanner.hasNextLine()) scanner.nextLine();

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] fields = line.split("~");
                data.add(fields);
            }
        }
        return data;
    }

    /**
     * Adds a few sample follow relationships so recommendations have data to work with.
     */
    private void seedFollows(Map<String, User> usersByName, FollowRepository followRepository) {
        User artWizard = usersByName.get("artwizard");
        if (artWizard == null) {
            return;
        }
        User travelMaster = usersByName.get("Travel-master-42");
        User coffeeNerd = usersByName.get("Coffee_nerd");
        User petMaster = usersByName.get("pet_master");
        User sunsetFan = usersByName.get("Sunset_enthusiast");
        User homeCook = usersByName.get("Homecookrunner");

        addFollow(travelMaster, artWizard, followRepository);
        addFollow(travelMaster, coffeeNerd, followRepository);
        addFollow(coffeeNerd, petMaster, followRepository);
        addFollow(petMaster, sunsetFan, followRepository);
        addFollow(homeCook, travelMaster, followRepository);
    }

    private void addFollow(User follower, User followee, FollowRepository followRepository) {
        if (follower == null || followee == null || follower.getId() == null || followee.getId() == null) {
            return;
        }
        if (followRepository.existsByFollowerAndFollowee(follower, followee)) {
            return;
        }
        followRepository.save(new Follow(follower, followee));
    }
}
