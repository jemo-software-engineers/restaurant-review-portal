package com.jemo.RestaurantReviewPortal.menu;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    Menu findByRestaurantIdAndId(Long restaurantId, Long Id);
    List<Menu> findAllByRestaurantId(Long restaurantId);
    Menu findByRestaurantIdAndName(Long restaurantId, String name);
}
