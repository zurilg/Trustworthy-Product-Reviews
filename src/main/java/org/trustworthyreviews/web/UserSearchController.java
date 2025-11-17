package org.trustworthyreviews.web;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.trustworthyreviews.User;
import org.trustworthyreviews.model.UserFollowerCountModel;
import org.trustworthyreviews.model.UserSimilarityModel;
import org.trustworthyreviews.repository.UserRepository;
import org.trustworthyreviews.service.CurrentUserService;
import org.trustworthyreviews.service.FollowService;
import org.trustworthyreviews.service.UserRelationshipService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UserSearchController {

    private final CurrentUserService currentUserService;
    private final UserRepository userRepository;
    private final UserRelationshipService userRelationshipService;
    private final FollowService followService;

    public UserSearchController(CurrentUserService currentUserService,
                                UserRepository userRepository,
                                UserRelationshipService userRelationshipService,
                                FollowService followService) {
        this.currentUserService = currentUserService;
        this.userRepository = userRepository;
        this.userRelationshipService = userRelationshipService;
        this.followService = followService;
    }

    @GetMapping("/users/similar")
    public String similarUsers(HttpSession session, Model model) {
        User currentUser = currentUserService.requireCurrentUser(session);
        List<UserSimilarityModel> similarities = userRepository.findAll().stream()
                .filter(u -> !u.getId().equals(currentUser.getId()))
                .map(u -> new UserSimilarityModel(u, userRelationshipService.getJaccardDistance(currentUser, u)))
                .sorted((a, b) -> Double.compare(a.distance(), b.distance()))
                .collect(Collectors.toList());

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("userSimilarities", similarities);
        model.addAttribute("categories", List.of());
        model.addAttribute("searchParams", "");
        model.addAttribute("currentCategory", null);
        return "pages/users-similar";
    }

    @GetMapping("/users/most-followed")
    public String mostFollowed(HttpSession session, Model model) {
        User currentUser = currentUserService.requireCurrentUser(session);
        List<UserFollowerCountModel> followerCounts = userRepository.findAll().stream()
                .filter(u -> !u.getId().equals(currentUser.getId()))
                .map(u -> new UserFollowerCountModel(u, followService.countFollowers(u)))
                .sorted((a, b) -> Long.compare(b.followerCount(), a.followerCount()))
                .collect(Collectors.toList());

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("followerCounts", followerCounts);
        model.addAttribute("categories", List.of());
        model.addAttribute("searchParams", "");
        model.addAttribute("currentCategory", null);
        return "pages/users-top";
    }
}

