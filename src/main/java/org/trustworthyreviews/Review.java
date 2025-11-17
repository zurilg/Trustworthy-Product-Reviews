package org.trustworthyreviews;
import java.time.Instant;
import java.util.*;
import jakarta.persistence.*;

/**
 * The Review entity represents a review made by a user for a product in the Trustworthy Reviews system.
 *
 * @version 11-17-2025
 */
@Entity
public class Review {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    private Product product;

    @ManyToOne
    private User author;

    private Instant createdAt;

    @Column(nullable = false)
    private int rating;

    private String content;

    /**
     * The empty constructor for JPA
     */
    public Review() {}

    /**
     * Constructor for Review
     *
     * @param id The unique identifier for the review
     * @param product The product the review belongs to
     * @param author The author of the review
     * @param createdAt The creation timestamp of the review
     * @param rating The rating given in the review
     * @param content The content of the review
     */
    public Review(UUID id, Product product, User author, Instant createdAt, int rating, String content) {
        this.id = id;
        this.product = product;
        this.author = author;
        this.createdAt = createdAt;
        this.rating = rating;
        this.content = content;
    }

    /**
     * Constructor for Review without id, rating, and content since they can be set later.
     *
     * @param product The product the review belongs to
     * @param author The author of the review
     * @param createdAt The creation timestamp of the review
     */
    public Review(Product product, User author, Instant createdAt) {
        this.product = product;
        this.author = author;
        this.createdAt = createdAt;
    }

    /**
     * Constructor for Review without id since it is generated.
     *
     * @param product The product the review belongs to
     * @param author The author of the review
     * @param createdAt The creation timestamp of the review
     * @param rating The rating given in the review
     * @param content The content of the review
     */
    public Review(Product product, User author, Instant createdAt, int rating, String content) {
        this.product = product;
        this.author = author;
        this.createdAt = createdAt;
        this.rating = rating;
        this.content = content;
    }

    /**
     * Getter for ID
     *
     * @return The unique identifier for the review
     */
    public UUID getId() {
        return id;
    }

    /**
     * The setter for ID
     *
     * @param id The unique identifier for the review
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Getter for the product the review belongs to
     *
     * @return The product the review belongs to
     */
    public Product getProduct() {
        return product;
    }

    /**
     * Setter for the product the review belongs to
     *
     * @param product The product the review belongs to
     */
    public void setProduct(Product product) {
        this.product = product;
    }

    /**
     * Getter for the author of the review
     *
     * @return The author of the review
     */
    public User getAuthor() {
        return author;
    }

    /**
     * Setter for the author of the review
     *
     * @param author The author of the review
     */
    public void setAuthor(User author) {
        this.author = author;
    }

    /**
     * Getter for the rating of the review
     *
     * @return The rating of the review
     */
    /*Getter for rating*/
    public int getRating() { return rating; }

    /**
     * Setter for the rating of the review
     *
     * @param rating The rating of the review
     */
    public void setRating(int rating) { this.rating = rating; }

    /**
     * Getter for the creation timestamp of the review
     *
     * @return The creation timestamp of the review
     */
    public Instant getCreatedAt() {
        return createdAt;
    }

    /**
     * Setter for the creation timestamp of the review
     *
     * @param createdAt The creation timestamp of the review
     */
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Getter for the content of the review
     *
     * @return The content of the review
     */
    public String getContent() {return content;}

    /**
     * Setter for the content of the review
     * @param content The content of the review
     */
    public void setContent(String content) {this.content = content;}
}
