package org.trustworthyreviews.service;

import org.trustworthyreviews.User;

import java.util.List;

/**
 * service for generating user follow recommendations (get DoS as a list for new UI)
 */
public interface RecommendationService {

    /**
     * Returns a list of users the provided user may want to follow.
     *
     * @param currentUser the logged-in user requesting recommendations
     * @param maxResults  maximum number of users to return
     * @return ordered list of recommended users (closest degree of separation first)
     */
    List<User> getRecommendedUsersToFollow(User currentUser, int maxResults);
}
