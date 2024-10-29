package com.jemo.RestaurantReviewPortal.restaurant;

public record RestaurantRequest(
        String name,
        String address,
        String phone,
        String email,
        String city,
        String website,
        String cuisine
) {}
