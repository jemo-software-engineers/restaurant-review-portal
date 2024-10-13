package com.jemo.RestaurantReviewPortal.menu;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MenuResponse {
    private Long id;
    private String name;
    private String description;
    private Long restaurantId;
}
