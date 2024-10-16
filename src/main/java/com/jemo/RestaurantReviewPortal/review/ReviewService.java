package com.jemo.RestaurantReviewPortal.review;

import com.jemo.RestaurantReviewPortal.menuitem.Menuitem;
import com.jemo.RestaurantReviewPortal.restaurant.Restaurant;
import com.jemo.RestaurantReviewPortal.user.User;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public List<Review> findAllByRestaurantId(Long restaurantId) {
        return reviewRepository.findAllByRestaurantIdAndStatus(restaurantId, ReviewStatus.valueOf("APPROVED"));
    }

    public List<Review> findAllByMenuitemId(Long menuitemId) {
        return reviewRepository.findAllByMenuitemIdAndStatus(menuitemId, ReviewStatus.valueOf("APPROVED"));
    }

    public Review findByRestaurantIdAndId(Long restaurantId, Long reviewId) {
        return reviewRepository.findByRestaurantIdAndId(restaurantId, reviewId);
    }

    public Review findByMenuitemIdAndId(Long menuitemId, Long reviewId) {
        return reviewRepository.findByMenuitemIdAndId(menuitemId, reviewId);
    }

    public Review findById(Long reviewId) {
        return reviewRepository.findById(reviewId).orElse(null);
    }

    @Transactional
    public Boolean deleteById(Long reviewId) {
        Review review = findById(reviewId);
        if (review != null) {
            reviewRepository.delete(review);
            return true;
        }
        return false;
    }

    @Transactional
    public Boolean updateById(Long reviewId, @Valid ReviewRequest reviewRequest) {
        Review review = findById(reviewId);
        if (review != null) {
            Review newReview = Review.builder()
                    .id(reviewId)
                    .restaurant(review.getRestaurant())
                    .user(review.getUser())
                    .reviewText(reviewRequest.reviewText())
                    .menuitem(review.getMenuitem())
                    .status(ReviewStatus.valueOf("PENDING"))
                    .build();
            reviewRepository.save(newReview);
            return true;
        }
        return false;
    }

    @Transactional
    public Boolean createReviewForRestaurant(Restaurant restaurant, @Valid ReviewRequest reviewRequest, User authenticatedUser) {
        // User cannot leave multiple reviews for a single restaurant. They can only update the initial review
        Review review = reviewRepository.findByRestaurantIdAndUserId(restaurant.getId(), authenticatedUser.getId());
        if(review != null) {
            return false;
        }

        Review reviewToCreate = Review.builder()
                .reviewText(reviewRequest.reviewText())
                .user(authenticatedUser)
                .menuitem(null)
                .status(ReviewStatus.valueOf("PENDING"))
                .restaurant(restaurant)
                .build();
        Review newReview = reviewRepository.save(reviewToCreate);
        return newReview.getId() != null ? true : false;
    }

    public Boolean createReviewForMenuitem(Menuitem menuitem, @Valid ReviewRequest reviewRequest, User authenticatedUser) {
        // User cannot leave multiple reviews for a single menuitem. They can only update the initial review
        Review review = reviewRepository.findByMenuitemIdAndUserId(menuitem.getId(), authenticatedUser.getId());
        if(review != null) {
            return false;
        }

        Review reviewToCreate = Review.builder()
                .reviewText(reviewRequest.reviewText())
                .user(authenticatedUser)
                .menuitem(menuitem)
                .status(ReviewStatus.valueOf("PENDING"))
                .restaurant(null)
                .build();
        Review newReview = reviewRepository.save(reviewToCreate);
        return newReview.getId() != null ? true : false;
    }


    public List<Review> findAllByPendingStatus() {
        return reviewRepository.findAllByStatus(ReviewStatus.PENDING);
    }


    public Boolean approveReview(Long reviewId, User user) {
        Review review = findById(reviewId);
        if (review != null) {
            review.setStatus(ReviewStatus.APPROVED);
            review.setApproved_by(user);
            reviewRepository.save(review);
            return true;
        }
        return false;
    }

    public Boolean rejectReview(Long reviewId, User user) {
        Review review = findById(reviewId);
        if (review != null) {
            review.setStatus(ReviewStatus.REJECTED);
            review.setApproved_by(user);

            reviewRepository.save(review);
            return true;
        }
        return false;
    }
}
