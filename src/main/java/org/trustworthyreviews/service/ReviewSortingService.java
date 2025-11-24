package org.trustworthyreviews.service;

import org.springframework.stereotype.Service;
import org.trustworthyreviews.Review;
import org.trustworthyreviews.User;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;


/**
 * Service for sorting reviews based on relevance to a specific user.
 *
 * @version 11-17-2025
 */
@Service
public class ReviewSortingService {
    // Can @Autowired a JaccardService or FollowingService here
    // For now, I'm keeping them nullable.
    private final JaccardDistanceProvider jaccardProvider;
    private final FollowingProvider followingProvider;

    /**
     * The constructor for ReviewSortingService.
     *
     * @param jaccardProvider The Jaccard distance provider
     * @param followingProvider The following relationship provider
     */
    public ReviewSortingService(
            JaccardDistanceProvider jaccardProvider,
            FollowingProvider followingProvider
    ) {
        this.jaccardProvider = jaccardProvider;
        this.followingProvider = followingProvider;
    }

    /**
     * Sorts reviews to be tailored to a specific logged-in user.
     *
     * @param reviews The list of reviews to sort
     * @param currentUser The current logged-in user
     */
    public List<Review> sortReviews(List<Review> reviews, User currentUser) {

        if (currentUser == null) {
            // If not logged in, default to newest-first.
            reviews.sort(Comparator.comparing(Review::getCreatedAt).reversed());
            return reviews;
        }

        reviews.sort((a, b) -> {

            double scoreA = score(a, currentUser);
            double scoreB = score(b, currentUser);

            return Double.compare(scoreB, scoreA); // descending
        });

        return reviews;
    }

    /**
     * Computes a relevance score per review.
     *
     * @param r The review to score
     * @param currentUser The current logged-in user
     */
    private double score(Review r, User currentUser) {
        double score = 0.0;

        // 1. Recency (always available)
        long ageSeconds = Instant.now().getEpochSecond() - r.getCreatedAt().getEpochSecond();
        score += Math.max(0, (1_000_000.0 / (ageSeconds + 1)));

        // 2. Rating bonus
        score += r.getRating() * 500000;

        // 3. Similarity
        if (jaccardProvider != null) {
            double similarity = jaccardProvider.similarity(currentUser.getId(), r.getAuthor().getId());
            score += similarity * 500;
        }

        // 4. Following boost
        if (followingProvider != null && followingProvider.isFollowing(currentUser.getId(), r.getAuthor().getId())) {
            score += 2000; // big boost
        }

        return score;
    }
    public interface JaccardDistanceProvider {
        double similarity(UUID userA, UUID userB);
    }

    public interface FollowingProvider {
        boolean isFollowing(UUID follower, UUID target);
    }
}
