package org.trustworthyreviews;
import java.time.Instant;
import java.util.*;
import jakarta.persistence.*;

@Entity
public class Product {
    @Id
    @GeneratedValue
    private UUID id;

    @OneToMany(mappedBy = "product")
    private List<Review> reviews;

    private String name;

    private String canonicalURL;

    private String category;

    private Instant createdAt;

    public Product() {}

    public Product(UUID id, String name, String canonicalURL, String category, Instant createdAt) {
        this.id = id;
        this.name = name;
        this.canonicalURL = canonicalURL;
        this.category = category;
        this.createdAt = createdAt;
    }

    /*Getter for id*/
    public UUID getId() {
        return id;
    }

    /*Setter for Id*/
    public void setId(UUID id) {
        this.id = id;
    }

    /*Getter for Name*/
    public String getName() {
        return name;
    }

    /*Setter for Name*/
    public void setName(String name) {
        this.name = name;
    }

    /*Getter for URL*/
    public String getCanonicalURL() {
        return canonicalURL;
    }

    /*Setter for URL*/
    public void setCanonicalURL(String canonicalURL) {
        this.canonicalURL = canonicalURL;
    }

    /*Getter for Category*/
    public String getCategory() {
        return category;
    }

    /*Setter for Category*/
    public void setCategory(String category) {
        this.category = category;
    }

    /*Getter for createdAt*/
    public Instant getCreatedAt() {
        return createdAt;
    }

    /*Setter for createdAt*/
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    /*Getter for Reviews*/
    public List<Review> getReviews() {
        return reviews;
    }

    /*Setter for Reviews*/
    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    /**
     * Adds a review to the product's reviews list
     *
     * @param review the review being added
     */
    public void addReview(Review review) {
        this.reviews.add(review);
    }

    /**
     * Removes a review to the product's reviews list
     *
     * @param review the review being removed
     */
    public void removeReview(Review review) {
        this.reviews.remove(review);
    }

    //TODO
    public double avgRating() {
        return 1;
    }

    /**
     * Returns the amount of reviews a product has
     *
     * @return the amount of reviews the product has
     */
    public int reviewsCount(){
        return reviews.size();
    }
}
