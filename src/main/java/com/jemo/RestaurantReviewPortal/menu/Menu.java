package com.jemo.RestaurantReviewPortal.menu;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Menu {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String description;
    private Long restaurantId;
}
