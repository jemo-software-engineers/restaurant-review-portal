package com.jemo.RestaurantReviewPortal.review;

import com.jemo.RestaurantReviewPortal.menuitem.Menuitem;
import com.jemo.RestaurantReviewPortal.menuitem.MenuitemService;
import com.jemo.RestaurantReviewPortal.rating.Rating;
import com.jemo.RestaurantReviewPortal.rating.RatingService;
import com.jemo.RestaurantReviewPortal.restaurant.Restaurant;
import com.jemo.RestaurantReviewPortal.restaurant.RestaurantService;
import com.jemo.RestaurantReviewPortal.user.User;
import com.jemo.RestaurantReviewPortal.user.UserRole;
import com.jemo.RestaurantReviewPortal.user.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    private final UserService userService;
    private final RestaurantService restaurantService;
    private final MenuitemService menuitemService;
    private final RatingService ratingService;


    //get all reviews for restaurant
    @GetMapping("/api/restaurants/{restaurantId}/reviews")
    public ResponseEntity<List<ReviewResponse>> getReviewsForRestaurant(@PathVariable Long restaurantId) {
        List<Review> reviews = reviewService.findAllByRestaurantId(restaurantId);

        return convertReviewListToReviewResponseList(reviews);
    }


    // get all reviews for menuitem
    @GetMapping("/api/menuitems/{menuitemId}/reviews")
    public ResponseEntity<List<ReviewResponse>> getReviewsForMenuitem(@PathVariable Long menuitemId) {
        List<Review> reviews = reviewService.findAllByMenuitemId(menuitemId);

        return convertReviewListToReviewResponseList(reviews);
    }

    // get single review for restaurant
    @GetMapping("/api/restaurants/{restaurantId}/reviews/{reviewId}")
    public ResponseEntity<ReviewResponse> getSingleReviewForSingleRestaurant(@PathVariable Long restaurantId, @PathVariable Long reviewId) {
        Review review = reviewService.findByRestaurantIdAndId(restaurantId, reviewId);
        if(review == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ReviewResponse reviewResponse = new ReviewResponse();
        reviewResponse.setId(review.getId());
        reviewResponse.setUserId(review.getUser().getId());
        reviewResponse.setUsername(review.getUser().getUsername());
        reviewResponse.setRestaurantId(review.getRestaurant().getId()); // check later. might need to make null
        reviewResponse.setRestaurantName(review.getRestaurant().getName());
        reviewResponse.setReviewText(review.getReviewText());
        reviewResponse.setAmbiance(review.getRating().getAmbiance());
        reviewResponse.setCleanlinessAndHygiene(review.getRating().getCleanlinessAndHygiene());
        reviewResponse.setCustomerService(review.getRating().getCustomerService());
        reviewResponse.setFoodQuality(review.getRating().getFoodQuality());
        reviewResponse.setValueForMoney(review.getRating().getValueForMoney());
        reviewResponse.setOverallRating(review.getRating().getOverallRating());
        reviewResponse.setStatus(review.getStatus());
        reviewResponse.setCreatedAt(review.getCreatedAt());

        return new ResponseEntity<>(reviewResponse, HttpStatus.OK);
    }

    // get single review for menuitem
    @GetMapping("/api/menuitems/{menuitemId}/reviews/{reviewId}")
    public ResponseEntity<ReviewResponse> getSingleReviewForSingleMenuitem(@PathVariable Long menuitemId, @PathVariable Long reviewId) {
        Review review = reviewService.findByMenuitemIdAndId(menuitemId, reviewId);
        if(review == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ReviewResponse reviewResponse = new ReviewResponse();
        reviewResponse.setId(review.getId());
        reviewResponse.setUserId(review.getUser().getId());
        reviewResponse.setUsername(review.getUser().getUsername());
        reviewResponse.setMenuitemId(review.getMenuitem().getId());  // check later. might need to make null
        reviewResponse.setMenuitemName(review.getMenuitem().getName());
        reviewResponse.setReviewText(review.getReviewText());
        reviewResponse.setCleanlinessAndHygiene(review.getRating().getCleanlinessAndHygiene());
        reviewResponse.setCustomerService(review.getRating().getCustomerService());
        reviewResponse.setFoodQuality(review.getRating().getFoodQuality());
        reviewResponse.setValueForMoney(review.getRating().getValueForMoney());
        reviewResponse.setAmbiance(review.getRating().getAmbiance());
        reviewResponse.setOverallRating(review.getRating().getOverallRating());
        reviewResponse.setStatus(review.getStatus());
        reviewResponse.setCreatedAt(review.getCreatedAt());

        return new ResponseEntity<>(reviewResponse, HttpStatus.OK);
    }


    // create review for restaurant
    @Transactional
    @PostMapping("/api/restaurants/{restaurantId}/reviews")
    public ResponseEntity<String> createReviewForRestaurant(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long restaurantId, @Valid @RequestBody ReviewRequest reviewRequest) {

        User authenticatedUser = userService.findByUsername(userDetails.getUsername());
        Restaurant restaurant = restaurantService.findById(restaurantId);
        if(restaurant == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Review reviewCreated = reviewService.createReviewForRestaurant(restaurant, reviewRequest, authenticatedUser);

        if(reviewCreated.getId() != null) {
            // implement rating
            Boolean rating = addRatingToReview(reviewCreated, reviewRequest);
            if(rating) {
                return new ResponseEntity<>("Review Created Successfully", HttpStatus.CREATED);
            }
        }
        return new ResponseEntity<>("Review Creation Failed", HttpStatus.BAD_REQUEST);
    }

    // create review for menuitem
    @Transactional
    @PostMapping("/api/menus/{menuId}/menuitems/{menuitemId}/reviews")
    public ResponseEntity<String> createReviewForMenuitem(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long menuId, @PathVariable Long menuitemId, @Valid @RequestBody ReviewRequest reviewRequest) {
        User authenticatedUser = userService.findByUsername(userDetails.getUsername());
        Menuitem menuitem = menuitemService.findMenuitemByMenuIdAndMenuitemId(menuId, menuitemId);
        if(menuitem == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Review reviewCreated = reviewService.createReviewForMenuitem(menuitem, reviewRequest, authenticatedUser);
        if(reviewCreated.getId() != null) {
            // implement rating
            Boolean rating = addRatingToReview(reviewCreated, reviewRequest);
            if(rating) {
                return new ResponseEntity<>("Review Created Successfully", HttpStatus.CREATED);
            }
        }
        return new ResponseEntity<>("Review Creation Failed", HttpStatus.BAD_REQUEST);
    }

    // update review
    @Transactional
    @PutMapping("/api/reviews/{reviewId}")
    public ResponseEntity<String> updateReview(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long reviewId, @Valid @RequestBody ReviewRequest reviewRequest) {
        Review reviewToUpdate = reviewService.findById(reviewId);
        User authenticatedUser = userService.findByUsername(userDetails.getUsername());
        if(reviewToUpdate == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            if(reviewToUpdate.getUser().getUsername().equals(authenticatedUser.getUsername()) || authenticatedUser.getRole().equals(UserRole.ADMIN)) {
                Review updated = reviewService.updateById(reviewId, reviewRequest);
                if(updated.getId() != null) {
                    // update the rating
                    Boolean ratingUpdated = ratingService.updateRatingForReview(updated, reviewRequest);
                    if(ratingUpdated) {
                        return new ResponseEntity<>("Review Updated Successfully", HttpStatus.OK);
                    }
                }
                return new ResponseEntity<>("Review Update Failed", HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }




    // delete review
    @Transactional
    @DeleteMapping("/api/reviews/{reviewId}")
    public ResponseEntity<String> deleteReview(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long reviewId) {
        Review reviewToDelete = reviewService.findById(reviewId);
        User authenticatedUser = userService.findByUsername(userDetails.getUsername());
        if(reviewToDelete == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            if(reviewToDelete.getUser().getUsername().equals(authenticatedUser.getUsername()) || authenticatedUser.getRole().equals(UserRole.ADMIN)) {
                Boolean deleted = reviewService.deleteById(reviewId);
                if(deleted) {
                    return new ResponseEntity<>("Review Deleted Successfully", HttpStatus.OK);
                }
                return new ResponseEntity<>("Review Delete Failed", HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // Get a list of all pending reviews due for approval
    @GetMapping("/admin/api/reviews")
    public ResponseEntity<List<ReviewResponse>> getAllPendingReviews() {
        List<Review> reviews = reviewService.findAllByPendingStatus();

        return convertReviewListToReviewResponseList(reviews);
    }

    // Approve Review Decision For ADMIN
    @Transactional
    @PutMapping("/admin/api/reviews/{reviewId}/approve")
    public ResponseEntity<String> approveReview(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long reviewId) {
        User authenticatedUser = userService.findByUsername(userDetails.getUsername());
        Review approvedReview = reviewService.approveReview(reviewId, authenticatedUser);
        if(approvedReview.getStatus() == ReviewStatus.APPROVED) {
                // recalculate average rating
                    // for restaurant
                if(approvedReview.getRestaurant() != null) {
                    List<Review> restaurantReviews = reviewService.findAllByRestaurantId(approvedReview.getRestaurant().getId());
                    Double averageRating = restaurantReviews.stream()
                            .mapToDouble(review -> {
                                return review.getRating().getOverallRating();
                            })
                            .average()
                            .orElse(0.0);

                    // call the restaurant service to update the average rating for restaurant
                    Boolean updated = restaurantService.updateRestaurantRating(approvedReview.getRestaurant().getId(), averageRating);
                    if(updated) {
                        return new ResponseEntity<>("Review Approved Successfully", HttpStatus.CREATED);
                    }
                }

            // recalculate average rating
            // for menuitem
            if(approvedReview.getMenuitem() != null) {
                List<Review> menuitemReviews = reviewService.findAllByMenuitemId(approvedReview.getMenuitem().getId());
                Double averageRating = menuitemReviews.stream()
                        .mapToDouble(review -> {
                            return review.getRating().getOverallRating();
                        })
                        .average()
                        .orElse(0.0);

                // call the menuitem service to update the average rating for menuitem
                Boolean updated = menuitemService.updateMenuitemRating(approvedReview.getMenuitem().getId(), averageRating);
                if(updated) {
                    return new ResponseEntity<>("Review Approved Successfully", HttpStatus.CREATED);
                }
            }

        }
        return new ResponseEntity<>("Review Approval Failed", HttpStatus.BAD_REQUEST);
    }

    // Reject Review Decision For ADMIN
    @Transactional
    @PutMapping("/admin/api/reviews/{reviewId}/reject")
    public ResponseEntity<String> rejectReview(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long reviewId) {
        User authenticatedUser = userService.findByUsername(userDetails.getUsername());
        Boolean rejected = reviewService.rejectReview(reviewId, authenticatedUser);
        if(rejected) {
            return new ResponseEntity<>("Review Rejected Successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Review Rejection Failed", HttpStatus.BAD_REQUEST);
    }







    // convert the list of reviews to review response list
    private ResponseEntity<List<ReviewResponse>> convertReviewListToReviewResponseList(List<Review> reviews) {
        List<ReviewResponse> reviewResponses = reviews.stream()
                .map(review -> {
                    ReviewResponse reviewResponse = new ReviewResponse();
                    reviewResponse.setId(review.getId());
                    reviewResponse.setUserId(review.getUser().getId());
                    reviewResponse.setUsername(review.getUser().getUsername());
                    reviewResponse.setRestaurantId(review.getRestaurant() != null ? review.getRestaurant().getId() : null); // check later. might need to make null
                    reviewResponse.setRestaurantName(review.getRestaurant()  != null ? review.getRestaurant().getName() : null);
                    reviewResponse.setMenuitemId(review.getMenuitem()  != null ? review.getMenuitem().getId() : null);
                    reviewResponse.setMenuitemName(review.getMenuitem() != null ? review.getMenuitem().getName() : null);
                    reviewResponse.setReviewText(review.getReviewText());
                    reviewResponse.setStatus(review.getStatus());
                    reviewResponse.setCreatedAt(review.getCreatedAt());
                    reviewResponse.setAmbiance(review.getRating().getAmbiance());
                    reviewResponse.setCustomerService(review.getRating().getCustomerService());
                    reviewResponse.setValueForMoney(review.getRating().getValueForMoney());
                    reviewResponse.setFoodQuality(review.getRating().getFoodQuality());
                    reviewResponse.setCleanlinessAndHygiene(review.getRating().getCleanlinessAndHygiene());
                    reviewResponse.setOverallRating(review.getRating().getOverallRating());
                    return reviewResponse;
                }).toList();
        return new ResponseEntity<>(reviewResponses, HttpStatus.OK);
    }

    private Boolean addRatingToReview(Review review, ReviewRequest reviewRequest) {
        Rating rating = ratingService.addRatingToReview(review, reviewRequest);
        if(rating.getId() != null) {
            return true;
        }
        return false;
    }

}
