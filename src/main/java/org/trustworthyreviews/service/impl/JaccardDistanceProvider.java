package org.trustworthyreviews.service.impl;

import org.springframework.stereotype.Service;
import org.trustworthyreviews.Follow;
import org.trustworthyreviews.Review;
import org.trustworthyreviews.User;
import org.trustworthyreviews.repository.FollowRepository;
import org.trustworthyreviews.repository.ReviewRepository;
import org.trustworthyreviews.repository.UserRepository;
import org.trustworthyreviews.service.ReviewSortingService;
import org.trustworthyreviews.service.UserRelationshipService;

import java.util.*;

@Service
public class JaccardDistanceProvider implements ReviewSortingService.JaccardDistanceProvider, UserRelationshipService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    public JaccardDistanceProvider(ReviewRepository reviewRepository,
                                   UserRepository userRepository,
                                   FollowRepository followRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.followRepository = followRepository;
    }

    @Override
    public double similarity(UUID userA, UUID userB) {
        if (userA == null || userB == null) {
            return 0.0;
        }
        User a = userRepository.findById(userA).orElse(null);
        User b = userRepository.findById(userB).orElse(null);
        return 1.0 - getJaccardDistance(a, b);
    }

    @Override
    public double getJaccardDistance(User a, User b) {
        if (a == null || b == null) {
            return 1.0;
        }
        if (a.getId() != null && a.getId().equals(b.getId())) {
            return 0.0;
        }

        Set<UUID> aProducts = reviewedProducts(a);
        Set<UUID> bProducts = reviewedProducts(b);

        if (aProducts.isEmpty() && bProducts.isEmpty()) {
            return 0.0;
        }

        Set<UUID> intersection = new HashSet<>(aProducts);
        intersection.retainAll(bProducts);

        Set<UUID> union = new HashSet<>(aProducts);
        union.addAll(bProducts);

        double similarity = union.isEmpty() ? 0.0 : (double) intersection.size() / union.size();
        return 1.0 - similarity;
    }

    @Override
    public int getDegreesOfSeparation(User a, User b) {
        if (a == null || b == null || a.getId() == null || b.getId() == null) {
            return -1;
        }
        if (a.getId().equals(b.getId())) {
            return 0;
        }

        Map<UUID, Set<UUID>> adjacency = buildAdjacency();

        Queue<UUID> queue = new ArrayDeque<>();
        Map<UUID, Integer> distances = new HashMap<>();

        queue.add(a.getId());
        distances.put(a.getId(), 0);

        while (!queue.isEmpty()) {
            UUID current = queue.poll();
            int distance = distances.get(current);
            for (UUID neighbor : adjacency.getOrDefault(current, Collections.emptySet())) {
                if (distances.containsKey(neighbor)) {
                    continue;
                }
                distances.put(neighbor, distance + 1);
                if (neighbor.equals(b.getId())) {
                    return distance + 1;
                }
                queue.add(neighbor);
            }
        }
        return -1;
    }

    private Set<UUID> reviewedProducts(User user) {
        if (user == null || user.getId() == null) {
            return Collections.emptySet();
        }
        List<Review> reviews = reviewRepository.findByAuthorId(user.getId());
        Set<UUID> productIds = new HashSet<>();
        for (Review review : reviews) {
            if (review.getProduct() != null && review.getProduct().getId() != null) {
                productIds.add(review.getProduct().getId());
            }
        }
        return productIds;
    }

    private Map<UUID, Set<UUID>> buildAdjacency() {
        Map<UUID, Set<UUID>> adjacency = new HashMap<>();
        List<Follow> follows = followRepository.findAll();
        for (Follow follow : follows) {
            UUID followerId = follow.getFollower() != null ? follow.getFollower().getId() : null;
            UUID followeeId = follow.getFollowee() != null ? follow.getFollowee().getId() : null;
            if (followerId == null || followeeId == null) {
                continue;
            }
            adjacency.computeIfAbsent(followerId, ignored -> new HashSet<>()).add(followeeId);
            adjacency.computeIfAbsent(followeeId, ignored -> new HashSet<>()).add(followerId);
        }
        return adjacency;
    }
}
