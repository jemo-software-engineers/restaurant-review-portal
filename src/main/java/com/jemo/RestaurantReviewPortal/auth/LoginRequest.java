package com.jemo.RestaurantReviewPortal.auth;

public record LoginRequest (
        String username,
        String password
) {}
