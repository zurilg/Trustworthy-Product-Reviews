package org.trustworthyreviews.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.trustworthyreviews.User;

import java.util.Optional;
import java.util.UUID;

/**
 * The UserRepository interface for managing User entities.
 *
 * @version 11-03-2025
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {
    /**
     * Find a user by their username.
     *
     * @param userName The username to search for
     * @return The Optional User object
     */
    Optional<User> findByUserName(String userName);

    /**
     * Find a user by their email.
     *
     * @param email The email to search for
     * @return The Optional User object
     */
    Optional<User> findByEmail(String email);

    /**
     * Check if a user exists by their email.
     *
     * @param email The email to check
     * @return true if a user with the given email exists, false otherwise
     */
    boolean existsByEmail(String email);
}