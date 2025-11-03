package org.trustworthyreviews;
import java.util.*;
import jakarta.persistence.*;

/**
 * The User entity represents a user in the Trustworthy Reviews system.
 *
 * @version 11-03-2025
 */
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

    /**
     * Empty constructor for JPA
     */
    public User() {}

    /**
     * Constructor for User
     *
     * @param userName The user's username
     * @param displayName The user's display name
     * @param email The user's email address
     */
    public User(String userName, String displayName, String email) {
        this.userName = userName;
        this.displayName = displayName;
        this.email = email;
    }

    /**
     * Getter for ID
     *
     * @return The unique identifier for the user
     */
    public UUID getId() {
        return id;
    }

    /**
     * Setter for ID
     *
     * @param id The unique identifier for the user
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Getter for Reviews
     *
     * @return The reviews written by the user
     */
    public List<Review> getReviews() {
        return reviews;
    }

    /**
     * Setter for Reviews
     *
     * @param reviews Reviews written by the user
     */
    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    /**
     * Getter for user's username
     *
     * @return The user's username
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Setter for user's username
     *
     * @param userName The user's username
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Getter for user's display name
     *
     * @return The user's display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Setter for user's display name
     *
     * @param displayName The user's display name
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Getter for user's email address
     *
     * @return The user's email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter for user's email address
     *
     * @param email The user's email address
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
