package org.trustworthyreviews.service;

import org.trustworthyreviews.User;

import java.util.Optional;
import java.util.UUID;

public interface UserInterface {
    Optional<User> get(UUID id);
    boolean emailExists(String email);
}
