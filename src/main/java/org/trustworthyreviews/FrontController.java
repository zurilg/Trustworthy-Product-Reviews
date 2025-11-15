package org.trustworthyreviews;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.trustworthyreviews.repository.ProductRepository;
import org.trustworthyreviews.repository.ReviewRepository;

import java.util.List;
import java.util.UUID;

/**
 * Front controller for handling web requests.
 *
 * @version 11-03-2025
 */
@Controller
public class FrontController {

    // Changed to constructor injection
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;

    /**
     * Constructor for FrontController.
     *
     * @param reviewRepository The review repository
     * @param productRepository The product repository
     */
    public FrontController(ReviewRepository reviewRepository, ProductRepository productRepository) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
    }

    /**
     * The home page handler.
     *
     * @param model The model to be used by the view.
     * @return The name of the view to be rendered.
     */
    @GetMapping("/")
    public String home(@RequestParam(required = false) String product_search, Model model) {
        List<Product> products;
        // Check if there is a valid product search query
        if (product_search != null && !product_search.isBlank()) {
            products = productRepository.findAllByNameContainingIgnoreCase(product_search)
                    .orElse(List.of());
            model.addAttribute("products", products);
            model.addAttribute("product_search", product_search);
        }
        // If no search query, show all products
        else{
            products = productRepository.findAll();
            model.addAttribute("products", products);
        }

        return "pages/home";
    }

    /**
     * The product page handler.
     *
     * @param productId The ID of the product
     * @param model The model to be used by the view
     * @return The name of the view to be rendered
     */
    @GetMapping("/product/{productId}")
    public String product(@PathVariable("productId") UUID productId, Model model) {

        Product p = productRepository.findById(productId).orElse(null);
        if (p == null) {
            return "redirect:/";
        }
        model.addAttribute("product", p);

        model.addAttribute("reviews", p.getReviews());

        model.addAttribute("pageNo", 1);
        model.addAttribute("pageMax", 1);

        User currentUser = new User("you", "YOU!!", "you@email.com");
        model.addAttribute("currentUser", currentUser);

        // Return the thymeleaf template
        return "pages/product";
    }
}
