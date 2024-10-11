package com.jemo.RestaurantReviewPortal.restaurant;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Restaurant findByEmail(String email);
    Restaurant findByName(String name);
}
