package org.trustworthyreviews.web;

import org.springframework.web.bind.annotation.*;
import org.trustworthyreviews.User;
import org.trustworthyreviews.repository.UserRepository;

@RestController
@RequestMapping ("/api/user")
public class UserAPI {
    private UserRepository userRepository;

    public UserAPI(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/login")
    public User login(@RequestParam("username") String username) {
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
