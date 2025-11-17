package org.trustworthyreviews.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.trustworthyreviews.User;
import org.trustworthyreviews.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Helper service to manage the logged-in user stored in the HTTP session.
 */
@Service
public class CurrentUserService {

    public static final String SESSION_USER_ID = "currentUserId";

    private final UserRepository userRepository;

    public CurrentUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void loginUser(HttpSession session, User user) {
        session.setAttribute(SESSION_USER_ID, user.getId());
    }

    public void logout(HttpSession session) {
        session.removeAttribute(SESSION_USER_ID);
    }

    public User getCurrentUser(HttpSession session) {
        if (session == null) {
            return null;
        }
        Object rawId = session.getAttribute(SESSION_USER_ID);
        if (!(rawId instanceof UUID userId)) {
            return null;
        }
        return userRepository.findById(userId).orElse(null);
    }

    public UUID getCurrentUserId(HttpSession session) {
        User user = getCurrentUser(session);
        return user != null ? user.getId() : null;
    }

    public User requireCurrentUser(HttpSession session) {
        User user = getCurrentUser(session);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not logged in");
        }
        return user;
    }
}

