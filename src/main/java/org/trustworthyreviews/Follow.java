package org.trustworthyreviews;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

/**
 * Follow represents a directed relationship where one user follows another.
 */
@Entity
@Table(name = "user_follow",
        uniqueConstraints = @UniqueConstraint(columnNames = {"follower_id", "followee_id"}))
public class Follow {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id")
    private User follower;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "followee_id")
    private User followee;

    private Instant createdAt = Instant.now();

    public Follow() {
    }

    public Follow(User follower, User followee) {
        this.follower = follower;
        this.followee = followee;
        this.createdAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public User getFollower() {
        return follower;
    }

    public void setFollower(User follower) {
        this.follower = follower;
    }

    public User getFollowee() {
        return followee;
    }

    public void setFollowee(User followee) {
        this.followee = followee;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}

