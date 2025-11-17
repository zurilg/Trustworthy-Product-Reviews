package org.trustworthyreviews.web;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.trustworthyreviews.User;
import org.trustworthyreviews.repository.UserRepository;
import org.trustworthyreviews.web.validation.LoginDTO;

/**
 * UserApi = REST controller for the website's users
 * GET /api/user/login: attempt to log in to a user's account and get their info
 *
 * @version 11-17-2025
 */
@RestController
@RequestMapping ("/api/user")
public class UserAPI {
    private UserRepository userRepository;

    public UserAPI(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * "Login" a user, actually just retrieves the user object for the provided the username
     * @param loginDTO The DTO containing a username
     * @return The User object of the logged-in user
     */
    @GetMapping("/login")
    public User login(@Valid LoginDTO loginDTO) {
        // Get the data from the DTO
        String username = loginDTO.getUsername();
        // Check if the user is not in the database
        if(userRepository.findByUserName(username).isEmpty()) {
            // Return an error
            throw new IllegalArgumentException("User not found");
        }
        // Return the object
        User u = userRepository.findByUserName(username).get();
        u.setReviews(null);
        return u;
    }

}
