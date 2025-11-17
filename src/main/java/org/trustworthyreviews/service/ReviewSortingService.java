package org.trustworthyreviews.service;

import org.springframework.stereotype.Service;
import org.trustworthyreviews.Review;
import org.trustworthyreviews.User;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;


@Service
public class ReviewSortingService {

    // Can @Autowired a JaccardService or FollowingService here
    // For now, I'm keeping them nullable.
    private final JaccardDistanceProvider jaccardProvider;
    private final FollowingProvider followingProvider;

    public ReviewSortingService(
            JaccardDistanceProvider jaccardProvider,
            FollowingProvider followingProvider
    ) {
        this.jaccardProvider = jaccardProvider;
        this.followingProvider = followingProvider;
    }

    /**
     * Sorts reviews to be tailored to a specific logged-in user.
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
     */
    private double score(Review r, User currentUser) {
        double score = 0.0;

        // 1. Recency (always available)
        long ageSeconds = Instant.now().getEpochSecond() - r.getCreatedAt().getEpochSecond();
        score += Math.max(0, (1_000_000.0 / (ageSeconds + 1)));

        // 2. Rating bonus
        score += r.getRating() * 10;

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
