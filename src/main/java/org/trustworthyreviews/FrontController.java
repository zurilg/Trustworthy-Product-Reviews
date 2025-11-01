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

import java.time.Instant;
import java.util.ArrayList;
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

        // Get the product using the productId and add to model

        // if (productRepository.findById(UUID.fromString(productId)).isEmpty()) {
        //     return "redirect:/";
        // }
        // Product p = productRepository.findById(UUID.fromString(productId)).get();
        // model.addAttribute("product", p);

        Product p = new Product(UUID.randomUUID(), "Small Crate", "https://www.homedepot.ca/product/hdx-heavy-duty-plastic-square-storage-organization-milk-crate-with-reinforced-handles/1000816782", "Storage" , Instant.now());
        model.addAttribute("product", p);

        // Get the reviews for the product and add to model with some page info

        // Potentially break this out into separate request in milestone 2 so this can be *reactive*
        // Pageable pageable = PageRequest.of(0, 10);
        // Page<Review> page = reviewRepository.findByProductId(UUID.fromString(productId), pageable);
        // model.addAttribute("reviews", page.getContent());
        // model.addAttribute("pageNo", page.getNumber());
        // model.addAttribute("pageMax", page.getTotalPages());
        User u = new User("user1", "User 1", "email@email.com");
        Review r1 = new Review(p, u, Instant.now());
        List<Review> reviews = new ArrayList<>();
        reviews.add(r1);

        System.out.println(reviews);
        model.addAttribute("reviews", reviews);
        model.addAttribute("pageNo", 1);
        model.addAttribute("pageMax", 1);

        // Get the current user from the email and add it to the model
        // This will be done in the next milestone and an email? will be supplied from the client as a cookie
        User currentUser = new User("you", "YOU!!", "you@email.com");
        model.addAttribute("currentUser", currentUser);

        // Return the thymeleaf template
        return "pages/product";
    }
}
