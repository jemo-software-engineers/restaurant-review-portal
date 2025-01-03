package com.jemo.RestaurantReviewPortal.restaurant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long>, JpaSpecificationExecutor<Restaurant> {
    Restaurant findByEmail(String email);
    Restaurant findByName(String name);
}
