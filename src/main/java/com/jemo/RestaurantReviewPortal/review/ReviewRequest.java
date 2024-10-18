package com.jemo.RestaurantReviewPortal.review;

public record ReviewRequest(
         String reviewText,
         Double foodQuality,
         Double customerService,
         Double cleanlinessAndHygiene,
         Double ambiance,
         Double valueForMoney
) {}
