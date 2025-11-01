package org.trustworthyreviews;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/")
    public String home(Model model) {
        // Get a list of products and add to the model
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);

        return "pages/home";
    }

    @GetMapping("/product/{productId}")
    public String product(@PathVariable String productId, Model model) {

        // Get the product and add to model
        if (productRepository.findById(UUID.fromString(productId)).isEmpty()) {
            return "redirect:/";
        }
        Product p = productRepository.findById(UUID.fromString(productId)).get();
        model.addAttribute("product", p);

        // Get the reviews for the product and add to model
        // Potentially break this out into separate request in milestone 2 so this can be *reactive*
        Pageable pageable = PageRequest.of(0, 10);
        Page<Review> page = reviewRepository.findByProductId(UUID.fromString(productId), pageable);
        model.addAttribute("reviews", page.getContent());
        model.addAttribute("pageNo", page.getNumber());
        model.addAttribute("pageMax", page.getTotalPages());

        // Return the thymeleaf template
        return "pages/product";
    }
}
