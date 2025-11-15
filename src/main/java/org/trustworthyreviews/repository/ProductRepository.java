package org.trustworthyreviews.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.trustworthyreviews.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The ProductRepository interface for managing Product entities.
 *
 * @version 11-03-2025
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {
    /**
     * Find a product by its name.
     *
     * @param name The name of the product
     * @return An Optional containing the product if found, or empty if not found
     */
    Optional<Product> findByName(String name);

    /**
     * Find all products with names containing the specified part, ignoring case.
     *
     * @param namePart Part of the name of the product
     * @return An Optional containing the product if found, or empty if not found
     */
    Optional<List<Product>> findAllByNameContainingIgnoreCase(String namePart);

    /**
     * Find products by their category.
     *
     * @param category The category of the products
     * @return A list of products in the specified category
     */
    List<Product> findByCategory(String category);

    /**
     * Find all products in a category with pagination, ordered by creation date descending.
     *
     * @param category The category of the products
     * @param pageable The pagination information
     * @return A page of products in the specified category
     */
    Page<Product> findAllByCategoryOrderByCreatedAtDesc(String category, Pageable pageable);
}
