package com.jemo.RestaurantReviewPortal.restaurant;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantResponse {
    private Long restaurantId;
    private String restaurantName;
    private String restaurantAddress;
    private String restaurantPhone;
    private String restaurantEmail;
    private String restaurantCity;
    private Double averageRating;
    private String website;
    private String cuisine;
    private String imageUrl;
}
