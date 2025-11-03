package org.trustworthyreviews;
import java.time.Instant;
import java.util.*;
import jakarta.persistence.*;

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
    private int rating;   // NEW: 1..5

    private String content; // NEW: review text content

    public Review() {}

    public Review(UUID id, Product product, User author, Instant createdAt, int rating, String content) {
        this.id = id;
        this.product = product;
        this.author = author;
        this.createdAt = createdAt;
        this.rating = rating;
        this.content = content;
    }

    public Review(Product product, User author, Instant createdAt) {
        this.product = product;
        this.author = author;
        this.createdAt = createdAt;
    }

    // Optional convenience constructor to set rating when constructing
    public Review(Product product, User author, Instant createdAt, int rating, String content) {
        this.product = product;
        this.author = author;
        this.createdAt = createdAt;
        this.rating = rating;
        this.content = content;
    }

    /*Getter for Id*/
    public UUID getId() {
        return id;
    }

    /*Setter for Id*/
    public void setId(UUID id) {
        this.id = id;
    }

    /*Getter for the product the review belongs to*/
    public Product getProduct() {
        return product;
    }

    /*Setter for the product the review belongs to*/
    public void setProduct(Product product) {
        this.product = product;
    }

    /*Getter for the review's author*/
    public User getAuthor() {
        return author;
    }

    /*Setter for the review's author*/
    public void setAuthor(User author) {
        this.author = author;
    }

    /*Getter for rating*/
    public int getRating() { return rating; }

    /*Setter for rating*/
    public void setRating(int rating) { this.rating = rating; }

    /*Getter for the createdAt*/
    public Instant getCreatedAt() {
        return createdAt;
    }

    /*Setter for the createdAt*/
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    /*Setter for the review's content*/
    public String getContent() {return content;}

    /*Getter for the review's content*/
    public void setContent(String content) {this.content = content;}
}
