package org.trustworthyreviews.service;

import org.trustworthyreviews.User;

import java.util.Set;
import java.util.UUID;

public interface FollowService extends ReviewSortingService.FollowingProvider {

    void follow(UUID followerId, UUID followeeId);

    void follow(User follower, User followee);

    void unfollow(User follower, User followee);

    boolean isFollowing(User follower, User followee);

    Set<UUID> getFolloweeIds(User follower);

    long countFollowers(User user);
}
