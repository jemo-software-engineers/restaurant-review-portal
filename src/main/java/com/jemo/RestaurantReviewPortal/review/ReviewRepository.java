package com.jemo.RestaurantReviewPortal.review;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByRestaurantIdAndStatus(Long restaurantId, ReviewStatus status);
    List<Review> findAllByMenuitemIdAndStatus(Long menuitemId, ReviewStatus status);
    Review findByRestaurantIdAndId(Long restaurantId, Long id);
    Review findByMenuitemIdAndUserId(Long menuitemId, Long id);
    Review findByRestaurantIdAndUserId(Long restaurantId, Long id);
    Review findByMenuitemIdAndId(Long menuitemId, Long reviewId);
    List<Review> findAllByStatus(ReviewStatus reviewStatus);
}
