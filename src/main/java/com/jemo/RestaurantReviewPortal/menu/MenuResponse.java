package com.jemo.RestaurantReviewPortal.menu;

import com.jemo.RestaurantReviewPortal.menuitem.MenuitemResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class MenuResponse {
    private Long id;
    private String name;
    private String description;
    private Long restaurantId;
    private String restaurantName;
    private List<MenuitemResponse> menuitems;
}
