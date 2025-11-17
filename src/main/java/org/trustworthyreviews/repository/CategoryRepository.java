package org.trustworthyreviews.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.trustworthyreviews.Category;

import java.util.Optional;
import java.util.UUID;

/**
 * The CategoryRepository interface for managing Category entities.
 *
 * @version 11-17-2025
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    @Query("SELECT c FROM Category c ORDER BY c.name ASC")
    Optional<Category> findByName(String name);
}
