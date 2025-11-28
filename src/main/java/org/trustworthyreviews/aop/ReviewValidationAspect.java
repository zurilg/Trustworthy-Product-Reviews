package org.trustworthyreviews.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import java.util.UUID;


/**
 * AOP Aspect for validating review inputs before creation or update.
 * This ensures that all reviews meet the required criteria before being processed.
 *
 * @version 11-17-2025
 */
@Aspect
@Component
public class ReviewValidationAspect {

    /**
     * AOP2 — Centralized input validation for review creation &
     * updates.
     * Applies to:
     *  - ReviewService.create(...)
     *  - ReviewService.updateOrCreate(...)
     */
    @Before("execution(* org.trustworthyreviews.service.ReviewService.create(..)) || " +
            "execution(* org.trustworthyreviews.service.ReviewService.updateOrCreate(..))")
    public void validateReviewInputs(JoinPoint jp) {

        Object[] args = jp.getArgs();
        UUID productId = (UUID) args[0];
        UUID authorId  = (UUID) args[1];
        int rating     = (int)  args[2];
        String content = (String) args[3];

        // Null checks
        if (productId == null) {
            throw new IllegalArgumentException("Product ID cannot be null.");
        }
        if (authorId == null) {
            throw new IllegalArgumentException("Author ID cannot be null.");
        }

        // Rating validation
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }

        // Content validation
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Review content cannot be empty.");
        }
        if (content.length() > 2000) {
            throw new IllegalArgumentException("Review content cannot exceed 2000 characters.");
        }
    }
}
