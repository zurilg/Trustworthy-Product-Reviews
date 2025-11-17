package org.trustworthyreviews.web;

import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.trustworthyreviews.User;
import org.trustworthyreviews.model.ReviewModel;
import org.trustworthyreviews.service.CurrentUserService;
import org.trustworthyreviews.service.ReviewService;

import java.util.UUID;

/**
 * REST API for creating, updating, listing, and deleting reviews.
 * Implements the update-or-create pattern and delete functionality
 * requested in the milestone issue.
 *
 * @version 11-17-2025
 */
@RestController
@RequestMapping("/api/reviews")
public class ReviewApi {

    private final ReviewService reviews;
    private final CurrentUserService currentUserService;

    public ReviewApi(ReviewService reviews, CurrentUserService currentUserService) {
        this.reviews = reviews;
        this.currentUserService = currentUserService;
    }

    /** CREATE review */
    @PostMapping
    public ReviewModel create(@RequestParam UUID productId,
                              @RequestParam int rating,
                              @RequestParam String content,
                              HttpSession session) {
        User current = currentUserService.requireCurrentUser(session);
        return reviews.create(productId, current.getId(), rating, content);
    }

    /**
     * UPDATE or CREATE review (PUT).
     * If the user already has a review for the product → update it.
     * If not → create a new review.
     */
    @PutMapping
    public ReviewModel updateOrCreate(@RequestParam UUID productId,
                                      @RequestParam int rating,
                                      @RequestParam String content,
                                      HttpSession session) {
        User current = currentUserService.requireCurrentUser(session);
        return reviews.updateOrCreate(productId, current.getId(), rating, content);
    }

    /** DELETE review by reviewId */
    @DeleteMapping
    public void delete(@RequestParam UUID reviewId, HttpSession session) {
        currentUserService.requireCurrentUser(session);
        reviews.delete(reviewId);
    }

    /** LIST reviews for a product (page optional) */
    @GetMapping("/product/{productId}")
    public Page<ReviewModel> listForProduct(@PathVariable UUID productId,
                                            @RequestParam(required = false) Integer page,
                                            @RequestParam(required = false) Integer size) {

        Pageable p = (page == null || size == null)
                ? null
                : PageRequest.of(page, size);

        return reviews.listForProduct(productId, p);
    }

    /** COUNT reviews */
    @GetMapping("/product/{productId}/count")
    public long countForProduct(@PathVariable UUID productId) {
        return reviews.countForProduct(productId);
    }
}
