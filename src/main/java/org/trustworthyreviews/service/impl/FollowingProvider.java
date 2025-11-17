package org.trustworthyreviews.service.impl;

import org.springframework.stereotype.Service;
import org.trustworthyreviews.service.ReviewSortingService;

import java.util.UUID;

@Service
public class FollowingProvider implements ReviewSortingService.FollowingProvider {

    @Override
    public boolean isFollowing(UUID follower, UUID target) {
        // TEMPORARY STUB
        return false;
    }
}
