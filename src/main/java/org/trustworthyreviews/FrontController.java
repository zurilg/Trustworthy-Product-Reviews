package org.trustworthyreviews;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.trustworthyreviews.repository.CategoryRepository;
import org.trustworthyreviews.repository.ProductRepository;
import org.trustworthyreviews.repository.ReviewRepository;
import org.trustworthyreviews.repository.UserRepository;
import org.trustworthyreviews.service.ReviewSortingService;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
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
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ReviewSortingService sortingService;


    /**
     * Constructor for FrontController.
     *
     * @param reviewRepository The review repository
     * @param productRepository The product repository
     */
    public FrontController(ReviewRepository reviewRepository, ProductRepository productRepository, UserRepository userRepository, CategoryRepository categoryRepository, ReviewSortingService sortingService) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.sortingService = sortingService;
    }

    /**
     * The home page handler.
     *
     * @param model The model to be used by the view.
     * @return The name of the view to be rendered.
     */
    @GetMapping("/")
    public String home(@RequestParam(required = false) String product_search,
                       @RequestParam(required = false) String category_name,
                       @CookieValue(name= "loggedin-uuid", required = false) UUID loggedInUser,
                       Model model) {

        User currentUser = null;
        if (loggedInUser != null) {
            currentUser = userRepository.findById(loggedInUser).orElse(null);
        }

        List<Product> products;

        Category currentCategory = null;
        if(category_name != null) {
            currentCategory = categoryRepository.findByName(category_name).orElse(null);
        }

        // Add necessary attributes to the model
        model.addAttribute("searchParams", product_search);
        model.addAttribute("currentCategory", currentCategory);
        model.addAttribute("categories", categoryRepository.findAll());

        // Determine which products to show based on search and category filters
        boolean hasSearch = product_search != null && !product_search.isBlank();
        boolean hasCategory = category_name != null && !category_name.isBlank();
        // If there is a search query, but no category selected
        if (hasSearch && !hasCategory) {
            products = productRepository.findAllByNameContainingIgnoreCase(product_search)
                    .orElse(List.of());
            model.addAttribute("products", products);
            model.addAttribute("product_search", product_search);
        }
        // If there is a category selected, but no search query
        else if (!hasSearch && hasCategory) {
            products = productRepository.findByCategory(currentCategory);
            model.addAttribute("products", products); // was commented out now restored
            for (Product prod : products) {
                List<Review> sorted = sortingService.sortReviews(prod.getReviews(), currentUser);
                if (!sorted.isEmpty()) {
                    prod.setReviews(List.of(sorted.get(0))); // show best review
                }
            }

            model.addAttribute("category_name", currentCategory.getName());
        }
        // If there is both a search query and a category selected
        else if(hasSearch && hasCategory) {
            products = productRepository.findAllByNameContainingIgnoreCaseAndCategoryNameIgnoreCase(product_search, currentCategory.getName())
                    .orElse(List.of());
            model.addAttribute("products", products);
            model.addAttribute("product_search", product_search);
            model.addAttribute("category_name", currentCategory.getName());
        }
        // If no search query and no category selected, show all products
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
     * @param loggedInUser The ID of the logged-in user
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

        model.addAttribute("searchParams", "");
        model.addAttribute("currentCategory", p.getCategory().getName());
        model.addAttribute("categories", categoryRepository.findAll());

        model.addAttribute("product", p);

        // If we have a logged-in user add them to the model
        User currentUser =  null;
        if (loggedInUser != null) {
            currentUser = userRepository.findById(loggedInUser).orElse(null);
        }
        model.addAttribute("currentUser", currentUser);

        //model.addAttribute("reviews", p.getReviews());

        List<Review> sorted = sortingService.sortReviews(p.getReviews(), currentUser);
        model.addAttribute("reviews", sorted);


        model.addAttribute("pageNo", 1);
        model.addAttribute("pageMax", 1);

        // If the user has a review add it to the model
        Review currentReview = null;
        boolean hasReview = false;
        if(loggedInUser != null) {
            List<Review> reviews = reviewRepository.findByAuthorId(loggedInUser);
            for(Review review : reviews) {
                if(review.getProduct().getId().equals(productId)) {
                    currentReview = review;
                    hasReview = true;
                }
            }
        }
        if(currentReview == null && loggedInUser != null) {
            // If there is no current review by the user then provide a blank one
            currentReview = new Review(p, currentUser, Instant.now());
            currentReview.setRating(2);
            currentReview.setContent("");
        }
        model.addAttribute("currentReview", currentReview);
        model.addAttribute("hasReview", hasReview);

        // Return the thymeleaf template
        return "pages/product";
    }

    /**
     * The add product page
     *
     * @param product The product to be added to the cite
     * @param model The model to be used by the view
     * @return The name of the view to be rendered
     */
    @GetMapping("/addProduct")
    public String addProductPage(@ModelAttribute("product") Product product, Model model) {
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryRepository.findAll());
        return "pages/addProduct";
    }

    /**
     * The request to add a product
     *
     * @param product The product to be added to the cite
     * @param categoryId The category of the product being added
     * @return Redirection to home page
     */
    @PostMapping("/addProduct")
    public String addProducts(@ModelAttribute("product") Product product, @RequestParam UUID categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElse(null);

        product.setCategory(category);
        product.setCreatedAt(Instant.now());

        productRepository.save(product);

        return "redirect:/";
    }
}
