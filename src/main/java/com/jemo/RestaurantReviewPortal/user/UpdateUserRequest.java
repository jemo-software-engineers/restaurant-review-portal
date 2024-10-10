package com.jemo.RestaurantReviewPortal.user;

public record UpdateUserRequest(
        String username,
        String password,
        String email
) {
}
