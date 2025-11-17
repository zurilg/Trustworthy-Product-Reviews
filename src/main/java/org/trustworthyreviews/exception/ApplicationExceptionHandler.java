package org.trustworthyreviews.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.trustworthyreviews.Category;
import org.trustworthyreviews.repository.CategoryRepository;
import org.trustworthyreviews.repository.ProductRepository;

/**
 * Centralized exception handler for the application. Handles specific exceptions and returns appropriate
 * models and views.
 * Handles IllegalSearchException by returning to the home page with an error message.
 *
 * @version 11-17-2025
 */
@ControllerAdvice
public class ApplicationExceptionHandler {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    /**
     * The constructor for ApplicationExceptionHandler.
     *
     * @param categoryRepository The category repository
     * @param productRepository The product repository
     */
    @Autowired
    public ApplicationExceptionHandler(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    /**
     * Initializes and returns a new model and thymeleaf template (view) for an illegal search exception.
     *
     * @param ex The illegal search exception
     * @return The model and view for the home page with error message
     */
    @ExceptionHandler(IllegalSearchException.class)
    public ModelAndView handleIllegalSearchException(IllegalSearchException ex) {
        ModelAndView mav = new ModelAndView("pages/home");
        mav.addObject("errorMessage", ex.getMessage()); // add error message
        mav.addObject("searchParams", ""); // clear search

        Category currentCategory = null;
        mav.addObject("categories", categoryRepository.findAll()); // load categories
        mav.addObject("currentCategory", null); // Reset to showing all products
        mav.addObject("products", productRepository.findAll()); // Reset to showing all products

        return mav;
    }
}
