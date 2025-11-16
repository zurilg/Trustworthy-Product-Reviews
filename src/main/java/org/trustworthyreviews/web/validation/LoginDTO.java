package org.trustworthyreviews.web.validation;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class LoginDTO {
    @NotNull
    @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters")
    private String username;

    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername() {
        return username;
    }
}
