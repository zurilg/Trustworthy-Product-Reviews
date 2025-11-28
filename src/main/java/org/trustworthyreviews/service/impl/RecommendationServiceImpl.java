package org.trustworthyreviews.service.impl;

import org.springframework.stereotype.Service;
import org.trustworthyreviews.Follow;
import org.trustworthyreviews.User;
import org.trustworthyreviews.repository.FollowRepository;
import org.trustworthyreviews.repository.UserRepository;
import org.trustworthyreviews.service.FollowService;
import org.trustworthyreviews.service.RecommendationService;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Degree of Separation based follow recommendations
 */
@Service
public class RecommendationServiceImpl implements RecommendationService {

    private static final int MAX_DEPTH = 3;

    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final FollowService followService;

    public RecommendationServiceImpl(FollowRepository followRepository,
                                     UserRepository userRepository,
                                     FollowService followService) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
        this.followService = followService;
    }

    @Override
    public List<User> getRecommendedUsersToFollow(User currentUser, int maxResults) {
        if (currentUser == null || currentUser.getId() == null || maxResults <= 0) {
            return List.of();
        }

        Map<UUID, User> usersById = userRepository.findAll().stream()
                .filter(u -> u.getId() != null)
                .collect(Collectors.toMap(User::getId, Function.identity()));

        Map<UUID, Integer> distances = bfsDistances(currentUser.getId(), buildAdjacency(), MAX_DEPTH);
        if (distances.isEmpty()) {
            return List.of();
        }

        Set<UUID> alreadyFollowing = followService.getFolloweeIds(currentUser);

        return distances.entrySet().stream()
                .filter(entry -> !entry.getKey().equals(currentUser.getId()))
                .map(entry -> new AbstractMap.SimpleEntry<>(usersById.get(entry.getKey()), entry.getValue()))
                .filter(entry -> entry.getKey() != null)
                .filter(entry -> !alreadyFollowing.contains(entry.getKey().getId()))
                .sorted(Comparator
                        .comparingInt(AbstractMap.SimpleEntry<User, Integer>::getValue)
                        .thenComparing(entry -> safeSortName(entry.getKey()), String.CASE_INSENSITIVE_ORDER)
                        .thenComparing(entry -> entry.getKey().getUserName(), String.CASE_INSENSITIVE_ORDER))
                .limit(maxResults)
                .map(Map.Entry::getKey)
                .toList();
    }

    private Map<UUID, Integer> bfsDistances(UUID start,
                                            Map<UUID, Set<UUID>> adjacency,
                                            int maxDepth) {
        if (!adjacency.containsKey(start)) {
            return Collections.emptyMap();
        }

        Map<UUID, Integer> distance = new HashMap<>();
        Queue<UUID> queue = new ArrayDeque<>();
        queue.add(start);
        distance.put(start, 0);

        while (!queue.isEmpty()) {
            UUID current = queue.poll();
            int currentDepth = distance.get(current);
            if (currentDepth >= maxDepth) {
                continue;
            }
            for (UUID neighbor : adjacency.getOrDefault(current, Collections.emptySet())) {
                if (distance.containsKey(neighbor)) {
                    continue;
                }
                distance.put(neighbor, currentDepth + 1);
                queue.add(neighbor);
            }
        }

        distance.remove(start);
        return distance;
    }

    private Map<UUID, Set<UUID>> buildAdjacency() {
        Map<UUID, Set<UUID>> adjacency = new HashMap<>();
        for (Follow follow : followRepository.findAll()) {
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

    private String safeSortName(User user) {
        if (user.getDisplayName() != null && !user.getDisplayName().isBlank()) {
            return user.getDisplayName();
        }
        if (user.getUserName() != null) {
            return user.getUserName();
        }
        return "";
    }
}
