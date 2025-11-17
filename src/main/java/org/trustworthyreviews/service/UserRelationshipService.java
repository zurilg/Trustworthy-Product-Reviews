package org.trustworthyreviews.service;

import org.trustworthyreviews.User;

public interface UserRelationshipService {

    double getJaccardDistance(User a, User b);

    int getDegreesOfSeparation(User a, User b);
}

