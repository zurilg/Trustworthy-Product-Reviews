package org.trustworthyreviews.web;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.trustworthyreviews.model.ReviewModel;
import org.trustworthyreviews.service.ReviewService;

import java.util.UUID;

/**
 * ReviewApi = REST controller exposing minimal endpoints the team can test with curl/Postman.
 * POST /api/reviews : create a review
 * GET  /api/reviews/product/{productId} : list reviews (pageable optional)
 * GET  /api/reviews/product/{productId}/count : count only
 * If 'page' or 'size' is missing, we pass null Pageable so the service applies defaults.
 *
 * @version 11-03-2025
 */
@RestController
@RequestMapping("/api/reviews")
public class ReviewApi {

    private final ReviewService reviews;

    public ReviewApi(ReviewService reviews) {
        this.reviews = reviews;
    }

    /**
     * Create review:
     * Example (curl):
     * curl -X POST "http://localhost:8080/api/reviews?productId=...&authorId=...&rating=5"
     */
    @PostMapping
    public ReviewModel create(@RequestParam UUID productId,
                              @RequestParam UUID authorId,
                              @RequestParam int rating) {
        return reviews.create(productId, authorId, rating);
    }

    /**
     * List reviews (page/size optional):
     * GET /api/reviews/product/{productId}?page=0&size=10
     * If page/size is not provided, Pageable stays null and the service uses a default.
     */
    @GetMapping("/product/{productId}")
    public Page<ReviewModel> listForProduct(@PathVariable UUID productId,
                                            @RequestParam(required = false) Integer page,
                                            @RequestParam(required = false) Integer size) {
        Pageable p = (page == null || size == null) ? null : PageRequest.of(page, size);
        return reviews.listForProduct(productId, p);
    }

    /**
     * Count reviews for a product:
     * GET /api/reviews/product/{productId}/count
     */
    @GetMapping("/product/{productId}/count")
    public long countForProduct(@PathVariable UUID productId) {
        return reviews.countForProduct(productId);
    }
}
