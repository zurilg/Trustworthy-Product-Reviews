package org.trustworthyreviews.service.impl;

import org.springframework.stereotype.Service;
import org.trustworthyreviews.Follow;
import org.trustworthyreviews.User;
import org.trustworthyreviews.repository.FollowRepository;
import org.trustworthyreviews.repository.UserRepository;
import org.trustworthyreviews.service.FollowService;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public FollowServiceImpl(FollowRepository followRepository, UserRepository userRepository) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void follow(User follower, User followee) {
        if (!isValidUsers(follower, followee) || isFollowing(follower, followee)) {
            return;
        }
        followRepository.save(new Follow(follower, followee));
    }

    @Override
    public void unfollow(User follower, User followee) {
        if (!isValidUsers(follower, followee)) {
            return;
        }
        followRepository.findByFollowerAndFollowee(follower, followee)
                .ifPresent(followRepository::delete);
    }

    @Override
    public boolean isFollowing(User follower, User followee) {
        if (!isValidUsers(follower, followee)) {
            return false;
        }
        return followRepository.existsByFollowerAndFollowee(follower, followee);
    }

    @Override
    public boolean isFollowing(UUID followerId, UUID targetId) {
        if (followerId == null || targetId == null) {
            return false;
        }
        if (followerId.equals(targetId)) {
            return false;
        }
        User follower = userRepository.findById(followerId).orElse(null);
        User followee = userRepository.findById(targetId).orElse(null);
        return isFollowing(follower, followee);
    }

    @Override
    public Set<UUID> getFolloweeIds(User follower) {
        if (follower == null || follower.getId() == null) {
            return Collections.emptySet();
        }
        return followRepository.findByFollower(follower).stream()
                .map(rel -> rel.getFollowee().getId())
                .collect(Collectors.toSet());
    }

    @Override
    public long countFollowers(User user) {
        if (user == null || user.getId() == null) {
            return 0;
        }
        return followRepository.countByFollowee(user);
    }

    private boolean isValidUsers(User follower, User followee) {
        return follower != null
                && followee != null
                && follower.getId() != null
                && followee.getId() != null
                && !follower.getId().equals(followee.getId());
    }
}
