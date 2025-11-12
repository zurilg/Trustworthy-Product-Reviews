package org.trustworthyreviews;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.trustworthyreviews.repository.ProductRepository;
import org.trustworthyreviews.repository.ReviewRepository;
import org.trustworthyreviews.repository.UserRepository;

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
    private final UserRepository userRepository;

    /**
     * Constructor for FrontController.
     *
     * @param reviewRepository The review repository
     * @param productRepository The product repository
     */
    public FrontController(ReviewRepository reviewRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    /**
     * The home page handler.
     *
     * @param model The model to be used by the view.
     * @return The name of the view to be rendered.
     */
    @GetMapping("/")
    public String home(Model model) {
        // Get a list of products and add to the model
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);

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
    public String product(
            @PathVariable("productId") UUID productId,
            @CookieValue(name = "loggedin-uuid", required = false) UUID loggedInUser,
            Model model) {

        Product p = productRepository.findById(productId).orElse(null);
        if (p == null) {
            return "redirect:/";
        }
        model.addAttribute("product", p);

        model.addAttribute("reviews", p.getReviews());

        model.addAttribute("pageNo", 1);
        model.addAttribute("pageMax", 1);

        User currentUser =  null;
        if (loggedInUser != null) {
            currentUser = userRepository.findById(loggedInUser).orElse(null);
        }
        model.addAttribute("currentUser", currentUser);

        // Return the thymeleaf template
        return "pages/product";
    }
}
