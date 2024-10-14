package com.jemo.RestaurantReviewPortal.menuitem;

import java.math.BigDecimal;

public record MenuitemRequest(
        String name,
        String description,
        BigDecimal price,
        Availability availability,
        String dietaryInfo
) {}
