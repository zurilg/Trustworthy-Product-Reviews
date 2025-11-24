package org.trustworthyreviews.web;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.trustworthyreviews.User;
import org.trustworthyreviews.repository.UserRepository;
import org.trustworthyreviews.service.CurrentUserService;
import org.trustworthyreviews.web.validation.LoginDTO;
import org.trustworthyreviews.web.validation.RegisterDTO;

/**
 * UserApi = REST controller for the website's users
 * GET /api/user/login: attempt to log in to a user's account and get their info
 *
 * @version 11-17-2025
 */
@RestController
@RequestMapping ("/api/user")
public class UserAPI {
    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;

    public UserAPI(UserRepository userRepository, CurrentUserService currentUserService) {
        this.userRepository = userRepository;
        this.currentUserService = currentUserService;
    }

    /**
     * "Login" a user, actually just retrieves the user object for the provided the username
     * @param loginDTO The DTO containing a username
     * @return The User object of the logged-in user
     */
    @PostMapping("/login")
    public User login(@Valid @RequestBody LoginDTO loginDTO, HttpSession session) {
        String username = loginDTO.getUsername();
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        currentUserService.loginUser(session, user);
        user.setReviews(null);
        return user;
    }

    @PostMapping("register")
    public ResponseEntity<User> register(@Valid @RequestBody RegisterDTO registerDTO) {
        String username = registerDTO.getUsername();
        if(userRepository.findByUserName(username).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is already taken");
        }

        User user = new User(registerDTO.getUsername(), registerDTO.getDisplayName(), registerDTO.getEmail());
        userRepository.save(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @GetMapping("/current")
    public ResponseEntity<User> current(HttpSession session) {
        User user = currentUserService.getCurrentUser(session);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        user.setReviews(null);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        currentUserService.logout(session);
        return ResponseEntity.ok().build();
    }
}
