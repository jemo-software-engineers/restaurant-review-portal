package com.jemo.RestaurantReviewPortal.menuitem;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class MenuitemResponse {
    private Long id;
    private String name;
    private String description;
    private Long menuId;
    private String menuName;
    private BigDecimal price;
    private Availability availability;
    private String dietaryInfo;
}
