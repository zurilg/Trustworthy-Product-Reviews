package org.trustworthyreviews.web.validation;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Login Data Transfer Object for validating login requests.
 *
 * @version 11-17-2025
 */
public class LoginDTO {
    @NotNull
    @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters")
    private String username;

    /**
     * Sets the username.
     *
     * @param username The username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the username.
     *
     * @return The username
     */
    public String getUsername() {
        return username;
    }
}
