package org.trustworthyreviews.service;

import org.springframework.stereotype.Service;
import org.trustworthyreviews.Review;
import org.trustworthyreviews.User;
import org.trustworthyreviews.repository.ReviewRepository;

import java.time.Instant;
import java.util.*;


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
    private final ReviewRepository reviewRepository;
    private static final Comparator<Instant> CREATED_DESC = Comparator.nullsLast(Comparator.<Instant>naturalOrder()).reversed();

    /**
     * The constructor for ReviewSortingService.
     *
     * @param jaccardProvider The Jaccard distance provider
     * @param followingProvider The following relationship provider
     */
    public ReviewSortingService(
            JaccardDistanceProvider jaccardProvider,
            FollowingProvider followingProvider,
            ReviewRepository reviewRepository
    ) {
        this.jaccardProvider = jaccardProvider;
        this.followingProvider = followingProvider;
        this.reviewRepository = reviewRepository;
    }

    /**
     * Sorts reviews to be tailored to a specific logged-in user.
     *
     * @param reviews The list of reviews to sort
     * @param currentUser The current logged-in user
     */
    public List<Review> sortReviews(List<Review> reviews, User currentUser) {
        return sortReviews(reviews, currentUser, ReviewSortType.DEFAULT);
    }

    public List<Review> sortReviews(List<Review> reviews, User currentUser, ReviewSortType sortType) {
        if (reviews == null) {
            return Collections.emptyList();
        }
        ReviewSortType type = sortType != null ? sortType : ReviewSortType.DEFAULT;

        switch (type) {
            case NEWEST -> sortByNewest(reviews);
            case HIGHEST_RATED -> sortByHighestRated(reviews);
            case RELEVANT -> sortByRelevance(reviews, currentUser);
            case DEFAULT -> sortByHybridScore(reviews, currentUser);
            default -> sortByHybridScore(reviews, currentUser);
        }
        return reviews;
    }

    private void sortByNewest(List<Review> reviews) {
        reviews.sort(Comparator.comparing(Review::getCreatedAt, CREATED_DESC));
    }

    private void sortByHighestRated(List<Review> reviews) {
        reviews.sort(
                Comparator.comparingInt(Review::getRating)
                        .reversed()
                        .thenComparing(Review::getCreatedAt, CREATED_DESC)
        );
    }

    private void sortByHybridScore(List<Review> reviews, User currentUser) {
        if (currentUser == null) {
            sortByNewest(reviews);
            return;
        }
        reviews.sort((a, b) -> {
            double scoreA = score(a, currentUser);
            double scoreB = score(b, currentUser);
            return Double.compare(scoreB, scoreA); // descending
        });
    }

    private void sortByRelevance(List<Review> reviews, User currentUser) {
        if (currentUser == null || currentUser.getId() == null) {
            sortByNewest(reviews);
            return;
        }
        Set<UUID> currentUserProducts = reviewedProductIds(currentUser);
        if (currentUserProducts.isEmpty()) {
            sortByNewest(reviews);
            return;
        }
        if (jaccardProvider == null) {
            sortByNewest(reviews);
            return;
        }

        Map<UUID, Double> authorDistances = new HashMap<>();
        for (Review review : reviews) {
            User author = review.getAuthor();
            UUID authorId = author != null ? author.getId() : null;
            if (authorId == null) {
                continue;
            }
            authorDistances.computeIfAbsent(authorId, ignored -> {
                Set<UUID> authorProducts = reviewedProductIds(author);
                return jaccardProvider.distance(currentUserProducts, authorProducts);
            });
        }

        reviews.sort(
                Comparator.comparing((Review review) -> {
                            User author = review.getAuthor();
                            UUID authorId = author != null ? author.getId() : null;
                            return authorDistances.getOrDefault(authorId, 1.0);
                        })
                        .thenComparing(Review::getCreatedAt, CREATED_DESC)
        );
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

    public double jaccardDistance(Set<UUID> a, Set<UUID> b) {
        return jaccardProvider != null ? jaccardProvider.distance(a, b) : 1.0;
    }

    private Set<UUID> reviewedProductIds(User user) {
        if (user == null) {
            return Collections.emptySet();
        }
        Set<UUID> ids = reviewRepository.findDistinctProductIdsReviewedByUser(user);
        return ids != null ? ids : Collections.emptySet();
    }

    public interface JaccardDistanceProvider {
        double similarity(UUID userA, UUID userB);
        double distance(Set<UUID> a, Set<UUID> b);
    }

    public interface FollowingProvider {
        boolean isFollowing(UUID follower, UUID target);
    }
}
