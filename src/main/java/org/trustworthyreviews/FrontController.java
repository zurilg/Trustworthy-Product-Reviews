package org.trustworthyreviews;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.trustworthyreviews.repository.CategoryRepository;
import jakarta.servlet.http.HttpSession;
import org.trustworthyreviews.repository.ProductRepository;
import org.trustworthyreviews.repository.ReviewRepository;
import org.trustworthyreviews.repository.UserRepository;
import org.trustworthyreviews.service.CurrentUserService;
import org.trustworthyreviews.service.FollowService;
import org.trustworthyreviews.service.RecommendationService;
import org.trustworthyreviews.service.ReviewSortingService;
import org.trustworthyreviews.service.ReviewSortType;
import org.trustworthyreviews.service.UserRelationshipService;
import org.trustworthyreviews.web.validation.RegisterDTO;

import java.time.Instant;
import java.util.*;

/**
 * Front controller for handling web requests.
 *
 * @version 11-17-2025
 */
@Controller
public class FrontController {

    // Changed to constructor injection
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ReviewSortingService sortingService;
    private final FollowService followService;
    private final UserRelationshipService userRelationshipService;
    private final CurrentUserService currentUserService;
    private final RecommendationService recommendationService;


    /**
     * Constructor for FrontController.
     *
     * @param reviewRepository The review repository
     * @param productRepository The product repository
     */
    public FrontController(ReviewRepository reviewRepository,
                           ProductRepository productRepository,
                           CategoryRepository categoryRepository,
                           UserRepository userRepository,
                           ReviewSortingService sortingService,
                           FollowService followService,
                           UserRelationshipService userRelationshipService,
                           CurrentUserService currentUserService,
                           RecommendationService recommendationService) {

        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.sortingService = sortingService;
        this.followService = followService;
        this.userRelationshipService = userRelationshipService;
        this.currentUserService = currentUserService;
        this.recommendationService = recommendationService;
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
                       HttpSession session,
                       Model model) {


        User currentUser = currentUserService.getCurrentUser(session);

        List<Product> products;

        Category currentCategory = null;
        if(category_name != null) {
            currentCategory = categoryRepository.findByName(category_name).orElse(null);
        }

        // Add necessary attributes to the model
        model.addAttribute("searchParams", product_search);
        model.addAttribute("currentCategory", currentCategory);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("currentUser", currentUser);

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

    @GetMapping("/profile/{userId}")
    public String profile(@PathVariable UUID userId, HttpSession session, Model model) {
        User user = userRepository.findById(userId).orElse(null);
        User currentUser = currentUserService.getCurrentUser(session);
        boolean isCurrentUser = currentUser != null && currentUser.getId().equals(userId);

        if (user == null) {
            return "redirect:/";
        }

        boolean isCurrentUserFollowingProfileOwner = false;
        if (currentUser != null && !isCurrentUser) {
            // Assuming followService.isFollowing(follower, followee) exists
            isCurrentUserFollowingProfileOwner = followService.isFollowing(currentUser, user);
        }

        model.addAttribute("user", user);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("reviews", reviewRepository.findByAuthorId(userId));
        model.addAttribute("isCurrentUser", isCurrentUser);
        model.addAttribute("isFollowingProfileOwner", isCurrentUserFollowingProfileOwner);
        model.addAttribute("categories", categoryRepository.findAll());

        List<User> followers = followService.getFollowers(user);
        List<User> following = followService.getFollowing(user);

        model.addAttribute("followers", followers);
        model.addAttribute("following", following);

        if(isCurrentUser){
            List<User> recommended = recommendationService.getRecommendedUsersToFollow(currentUser, 8);
            Map<UUID, Integer> recommendedDistances = new HashMap<>();
            for (User u : recommended) {
                if (u != null && u.getId() != null) {
                    recommendedDistances.put(u.getId(), userRelationshipService.getDegreesOfSeparation(currentUser, u));
                }
            }
            model.addAttribute("categories", categoryRepository.findAll());
            model.addAttribute("searchParams", "");
            model.addAttribute("currentCategory", null);
            model.addAttribute("recommendedUsers", recommended);
            model.addAttribute("recommendedDistances", recommendedDistances);
        } else {
            model.addAttribute("recommendedUsers", List.of());
            model.addAttribute("recommendedDistances", Map.of());
        }

        return "pages/profile";
    }

    /**
     * The product page handler
     *
     * @param productId The ID of the product
     * @param model The model to be used by the view
     * @return The name of the view to be rendered
     */
    @GetMapping("/product/{productId}")
    public String product(
            @PathVariable("productId") UUID productId,
            @RequestParam(value = "sort", required = false) String sort,
            HttpSession session,
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
        User currentUser = currentUserService.getCurrentUser(session);
        model.addAttribute("currentUser", currentUser);

        //model.addAttribute("reviews", p.getReviews());

        ReviewSortType sortType = resolveSortType(sort);
        List<Review> sorted = sortingService.sortReviews(p.getReviews(), currentUser, sortType);
        model.addAttribute("reviews", sorted);
        model.addAttribute("sortType", sortType);
        model.addAttribute("reviewSimilarities", buildSimilarityScores(sorted, currentUser, sortType));

        Map<UUID, Integer> separation = new HashMap<>();
        Set<UUID> followedUserIds = Collections.emptySet();
        if (currentUser != null) {
            followedUserIds = followService.getFolloweeIds(currentUser);
            for (Review review : sorted) {
                User author = review.getAuthor();
                if (author != null && author.getId() != null) {
                    separation.put(author.getId(), userRelationshipService.getDegreesOfSeparation(currentUser, author));
                }
            }
        }

        model.addAttribute("followedUserIds", followedUserIds);
        model.addAttribute("degreesOfSeparation", separation);


        model.addAttribute("pageNo", 1);
        model.addAttribute("pageMax", 1);

        // If the user has a review add it to the model
        Review currentReview = null;
        boolean hasReview = false;
        if(currentUser != null) {
            List<Review> reviews = reviewRepository.findByAuthorId(currentUser.getId());
            for(Review review : reviews) {
                if(review.getProduct().getId().equals(productId)) {
                    currentReview = review;
                    hasReview = true;
                }
            }
        }
        if(currentReview == null && currentUser != null) {
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

    //handles sort type
    private ReviewSortType resolveSortType(String sortParam) {
        if (sortParam == null || sortParam.isBlank()) {
            return ReviewSortType.DEFAULT;
        }
        try {
            return ReviewSortType.valueOf(sortParam.toUpperCase());
        } catch (IllegalArgumentException ex) {
            return ReviewSortType.DEFAULT;
        }
    }

    private Map<UUID, Double> buildSimilarityScores(List<Review> reviews, User currentUser, ReviewSortType sortType) {
        if (sortType != ReviewSortType.RELEVANT || currentUser == null || currentUser.getId() == null) {
            return Collections.emptyMap();
        }
        Set<UUID> currentUserProducts = reviewRepository.findDistinctProductIdsReviewedByUser(currentUser);
        if (currentUserProducts == null || currentUserProducts.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<UUID, Double> scores = new HashMap<>();
        for (Review review : reviews) {
            if (review.getId() == null) {
                continue;
            }
            User author = review.getAuthor();
            if (author == null || author.getId() == null) {
                continue;
            }
            Set<UUID> authorProducts = reviewRepository.findDistinctProductIdsReviewedByUser(author);
            double distance = sortingService.jaccardDistance(currentUserProducts, authorProducts);
            scores.put(review.getId(), 1.0 - distance);
        }
        return scores;
    }

    @GetMapping("registerUser")
    public String userRegistration() {
        return "pages/registerUser";
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
