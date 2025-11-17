package org.trustworthyreviews.web;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.trustworthyreviews.User;
import org.trustworthyreviews.repository.UserRepository;
import org.trustworthyreviews.service.CurrentUserService;
import org.trustworthyreviews.service.FollowService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/follow")
public class FollowApi {

    private final FollowService followService;
    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;

    public FollowApi(FollowService followService,
                     UserRepository userRepository,
                     CurrentUserService currentUserService) {
        this.followService = followService;
        this.userRepository = userRepository;
        this.currentUserService = currentUserService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> follow(@RequestParam UUID followeeId,
                                                      HttpSession session) {
        User follower = currentUserService.getCurrentUser(session);
        if (follower == null) {
            return unauthorizedResponse();
        }
        User followee = userRepository.findById(followeeId).orElse(null);
        if (followee == null || followee.getId().equals(follower.getId())) {
            return errorResponse("User not found");
        }
        followService.follow(follower, followee);
        return okResponse(true);
    }

    @PostMapping("/unfollow")
    public ResponseEntity<Map<String, Object>> unfollow(@RequestParam UUID followeeId,
                                                        HttpSession session) {
        User follower = currentUserService.getCurrentUser(session);
        if (follower == null) {
            return unauthorizedResponse();
        }
        User followee = userRepository.findById(followeeId).orElse(null);
        if (followee == null || followee.getId().equals(follower.getId())) {
            return errorResponse("User not found");
        }
        followService.unfollow(follower, followee);
        return okResponse(false);
    }

    private ResponseEntity<Map<String, Object>> okResponse(boolean following) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("status", "ok");
        payload.put("following", following);
        return ResponseEntity.ok(payload);
    }

    private ResponseEntity<Map<String, Object>> unauthorizedResponse() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("status", "error");
        payload.put("message", "Login required");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(payload);
    }

    private ResponseEntity<Map<String, Object>> errorResponse(String message) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("status", "error");
        payload.put("message", message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(payload);
    }
}

