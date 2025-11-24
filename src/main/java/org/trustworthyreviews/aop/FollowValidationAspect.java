package org.trustworthyreviews.aop;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.trustworthyreviews.repository.FollowRepository;

import java.util.UUID;

@Aspect
@Component
public class FollowValidationAspect {

    private static final Logger log = LoggerFactory.getLogger(FollowValidationAspect.class);

    private final FollowRepository followRepository;

    public FollowValidationAspect(FollowRepository followRepository) {
        this.followRepository = followRepository;
    }

    @Pointcut("execution(* org.trustworthyreviews.service.FollowService.follow(java.util.UUID, java.util.UUID))")
    private void followById() {
    }

    @Before(value = "followById() && args(followerId, followeeId)", argNames = "followerId,followeeId")
    public void validateFollow(UUID followerId, UUID followeeId) {
        if (followerId == null || followeeId == null) {
            throw new IllegalArgumentException("Follower and followee must be provided.");
        }
        if (followerId.equals(followeeId)) {
            throw new IllegalArgumentException("You cannot follow yourself.");
        }
        if (followRepository.existsByFollowerIdAndFolloweeId(followerId, followeeId)) {
            throw new IllegalStateException("You already follow this user.");
        }
    }

    @AfterReturning(value = "followById() && args(followerId, followeeId)", argNames = "followerId,followeeId")
    public void logFollow(UUID followerId, UUID followeeId) {
        log.info("User {} followed {}", followerId, followeeId);
    }
}

