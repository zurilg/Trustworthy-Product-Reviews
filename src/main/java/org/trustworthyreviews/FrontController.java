package org.trustworthyreviews;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.trustworthyreviews.repository.ProductRepository;
import org.trustworthyreviews.repository.ReviewRepository;

import java.util.List;
import java.util.UUID;

@Controller
public class FrontController {

    // Changed to constructor injection
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;

    public FrontController(ReviewRepository reviewRepository, ProductRepository productRepository) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
    }

    @GetMapping("/")
    public String home(Model model) {
        // Get a list of products and add to the model
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);

        return "pages/home";
    }

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
