package org.trustworthyreviews.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trustworthyreviews.User;
import org.trustworthyreviews.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class UserService implements UserInterface {

    private final UserRepository users;

    public UserService(UserRepository users) {
        this.users = users;
    }

    @Override
    public Optional<User> get(UUID id) {
        return users.findById(id);
    }

    @Override
    public boolean emailExists(String email) {
        return users.existsByEmail(email);
    }
}
