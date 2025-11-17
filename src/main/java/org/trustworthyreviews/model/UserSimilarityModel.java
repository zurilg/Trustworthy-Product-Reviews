package org.trustworthyreviews.model;

import org.trustworthyreviews.User;

/**
 * User similarity model to inject jaccard distance into front end
 * @param user
 * @param distance
 */
public record UserSimilarityModel(User user, double distance) {
}

