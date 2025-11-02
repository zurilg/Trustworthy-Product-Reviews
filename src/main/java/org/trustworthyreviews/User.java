package org.trustworthyreviews;
import java.util.*;
import jakarta.persistence.*;

@Entity
@Table(name = "APP_USER") // Added to ensure test class passes
public class User {
    @Id
    @GeneratedValue
    private UUID id;

    // NEW: added cascade and orphanRemoval to manage reviews lifecycle
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;

    private String userName;

    private String displayName;

    private String email;

    public User() {}

    public User(String userName, String displayName, String email) {
        this.userName = userName;
        this.displayName = displayName;
        this.email = email;
    }

    /*Getter for Id*/
    public UUID getId() {
        return id;
    }

    /*Setter for Id*/
    public void setId(UUID id) {
        this.id = id;
    }

    /*Getter for Reviews*/
    public List<Review> getReviews() {
        return reviews;
    }

    /*Setter for Reviews*/
    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    /*Getter for userName*/
    public String getUserName() {
        return userName;
    }

    /*Getter for userName*/
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /*Getter for displayName*/
    public String getDisplayName() {
        return displayName;
    }

    /*Setter for displayName*/
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /*Getter for user email*/
    public String getEmail() {
        return email;
    }

    /*Getter for user email*/
    public void setEmail(String email) {
        this.email = email;
    }

    // Extra Functions --------------------------------------------
    public void addReview(Review review) {
        if (this.reviews == null) {
            this.reviews = new ArrayList<>();
        }
        this.reviews.add(review);
    }
}
