package org.trustworthyreviews;
import java.util.*;
import jakarta.persistence.*;

@Entity
public class User {
    @Id
    @GeneratedValue
    private UUID id;

    @OneToMany(mappedBy = "author")
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
}
