package org.trustworthyreviews;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.trustworthyreviews.model.ReviewModel;
import org.trustworthyreviews.repository.ProductRepository;
import org.trustworthyreviews.repository.UserRepository;
import org.trustworthyreviews.service.ReviewService;
import org.trustworthyreviews.service.impl.ReviewServiceImpl;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Lightweight service test
 * Uses @DataJpaTest with @Import(ServiceImpl) so we can run against H2/JPA quickly.
 * Verifies create + list + count + DTO mapping (no web server needed).
 *
 * @version 11-03-2025
 */
@DataJpaTest
@Import(ReviewServiceImpl.class)
class ReviewServiceImplTest {

    @Autowired
    private ReviewService service;
    @Autowired
    private ProductRepository products;
    @Autowired
    private UserRepository users;

    /**
     * Test method for create() and listForProduct().
     */
    @Test
    void createAndList_modelsNotEntities() {
        // Arrange: seed a product + user quickly
        var p = new Product();
        p.setName("P1");
        p.setCategory("Cat");
        p.setCanonicalURL("https://x");
        p.setCreatedAt(Instant.now());
        p = products.save(p);

        var u = users.save(new User("jdoe", "John", "j@e.com"));

        // Act: create one review + list
        ReviewModel created = service.create(p.getId(), u.getId(), 5);
        var page = service.listForProduct(p.getId(), PageRequest.of(0, 10));

        // Assert: DTO has expected fields; count matches
        assertEquals(5, created.getRating());
        assertEquals(p.getId(), created.getProductId());
        assertEquals(u.getId(), created.getAuthorId());
        assertEquals(1, page.getTotalElements());
        assertEquals(1, service.countForProduct(p.getId()));
    }
}
