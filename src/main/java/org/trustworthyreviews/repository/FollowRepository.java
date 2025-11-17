package org.trustworthyreviews.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.trustworthyreviews.Follow;
import org.trustworthyreviews.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for Follow relationships.
 */
@Repository
public interface FollowRepository extends JpaRepository<Follow, UUID> {

    List<Follow> findByFollower(User user);

    List<Follow> findByFollowee(User user);

    Optional<Follow> findByFollowerAndFollowee(User follower, User followee);

    boolean existsByFollowerAndFollowee(User follower, User followee);

    long countByFollowee(User followee);
}
