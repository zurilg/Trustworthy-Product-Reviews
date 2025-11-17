package org.trustworthyreviews.model;

import org.trustworthyreviews.User;

/**
 * User follower count model to inject
 * @param user
 * @param followerCount
 */
public record UserFollowerCountModel(User user, long followerCount) {
}

