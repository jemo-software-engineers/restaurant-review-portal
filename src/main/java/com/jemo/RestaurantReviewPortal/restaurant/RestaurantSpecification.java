package com.jemo.RestaurantReviewPortal.restaurant;

import org.springframework.data.jpa.domain.Specification;

public class RestaurantSpecification {
    public static Specification<Restaurant> hasName(String name) {
        return (root, query, criteriaBuilder) ->
                name == null ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Restaurant> hasCity(String city) {
        return (root, query, criteriaBuilder) ->
                city == null ? null : criteriaBuilder.equal(criteriaBuilder.lower(root.get("city")), city.toLowerCase());
    }

    public static Specification<Restaurant> hasCuisine(String cuisine) {
        return (root, query, criteriaBuilder) ->
                cuisine == null ? null : criteriaBuilder.equal(criteriaBuilder.lower(root.get("cuisine")), cuisine.toLowerCase());
    }
}
