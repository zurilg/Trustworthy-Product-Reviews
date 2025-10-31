package org.trustworthyreviews.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.trustworthyreviews.Review;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID>, JpaSpecificationExecutor<Review> {
    List<Review> findByProductIdOrderByCreatedAtDesc(UUID productId);

    Page<Review> findByProductId(UUID productId, Pageable pageable);

    long countByProductId(UUID productId);

    List<Review> findByAuthorId(UUID authorId);
}