package com.jemo.RestaurantReviewPortal.review;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReviewResponse {
    private Long id;
    private String reviewText;
    private Long userId;
    private String username;
    private Long restaurantId;
    private String restaurantName;
    private Long menuitemId;
    private String menuitemName;
    private ReviewStatus status;
    private LocalDateTime createdAt;


}
