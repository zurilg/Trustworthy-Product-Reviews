package org.trustworthyreviews;
import java.time.Instant;
import java.util.*;
import jakarta.persistence.*;

/**
 * The Product entity represents a product in the Trustworthy Reviews system.
 *
 * @version 11-03-2025
 */
@Entity
public class Product {
    @Id
    @GeneratedValue
    private UUID id;

    @OneToMany(mappedBy = "product")
    private List<Review> reviews = new ArrayList<>();

    private String name;
    private String canonicalURL;
    private String pictureURL;
    private String category;
    private Instant createdAt;

    /**
     * Empty constructor for JPA
     */
    public Product() {}

    /**
     * Constructor for Product
     *
     * @param id The unique identifier for the product
     * @param name The name of the product
     * @param canonicalURL The canonical URL of the product
     * @param pictureURL The picture URL of the product
     * @param category The category of the product
     * @param createdAt The creation timestamp of the product
     */
    public Product(UUID id, String name, String canonicalURL, String pictureURL, String category, Instant createdAt) {
        this.id = id;
        this.name = name;
        this.canonicalURL = canonicalURL;
        this.pictureURL = pictureURL;
        this.category = category;
        this.createdAt = createdAt;
    }

    /**
     * Constructor for Product without id since it is generated
     *
     * @param name The name of the product
     * @param canonicalURL The canonical URL of the product
     * @param pictureURL The picture URL of the product
     * @param category The category of the product
     * @param createdAt The creation timestamp of the product
     */
    public Product(String name, String canonicalURL, String pictureURL, String category, Instant createdAt) {
        this.name = name;
        this.canonicalURL = canonicalURL;
        this.pictureURL = pictureURL;
        this.category = category;
        this.createdAt = createdAt;
    }

    /**
     * Getter for Id
     *
     * @return The unique identifier for the product
     */
    public UUID getId() {
        return id;
    }

    /**
     * Setter for Id
     *
     * @param id The unique identifier for the product
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Getter for Name
     *
     * @return The name of the product
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for Name
     *
     * @param name The name of the product
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for canonical URL
     *
     * @return The canonical URL of the product
     */
    public String getCanonicalURL() {
        return canonicalURL;
    }

    /**
     * Setter for canonical URL
     *
     * @param canonicalURL The canonical URL of the product
     */
    public void setCanonicalURL(String canonicalURL) {
        this.canonicalURL = canonicalURL;
    }

    /**
     * Getter for Picture URL
     *
     * @return The picture URL of the product
     */
    public String getPictureURL() {
        return pictureURL;
    }

    /**
     * Setter for Picture URL
     *
     * @param pictureURL The picture URL of the product
     */
    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }

    /**
     * Getter for Category
     *
     * @return The category of the product
     */
    public String getCategory() {
        return category;
    }

    /**
     * Setter for Category
     *
     * @param category The category of the product
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Getter for createdAt
     *
     * @return The creation timestamp of the product
     */
    public Instant getCreatedAt() {
        return createdAt;
    }

    /**
     * Setter for createdAt
     *
     * @param createdAt The creation timestamp of the product
     */
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Getter for Reviews
     *
     * @return The list of reviews for the product
     */
    public List<Review> getReviews() {
        return reviews;
    }

    /**
     * Setter for Reviews
     *
     * @param reviews The list of reviews for the product
     */
    /*Setter for Reviews*/
    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    /**
     * Adds a review to the product's reviews list
     *
     * @param review The review being added
     */
    public void addReview(Review review) {
        this.reviews.add(review);
    }

    /**
     * Removes a review to the product's reviews list
     *
     * @param review The review being removed
     */
    public void removeReview(Review review) {
        this.reviews.remove(review);
    }

    /**
     * Calculates the average rating of the product based on its reviews.
     *
     * @return The average rating of the product based on its reviews
     */
    public double avgRating() {
        double total = 0.0;
        if (reviews.isEmpty()) {
            return 0.0;
        } else {
            for (Review review : reviews) {
                total += review.getRating();
            }
        }
        return total / reviews.size();
    }

    /**
     * Returns the amount of reviews a product has
     *
     * @return The amount of reviews the product has
     */
    public int reviewsCount(){
        return reviews.size();
    }
}
