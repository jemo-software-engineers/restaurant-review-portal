package com.jemo.RestaurantReviewPortal.menuitem;

import org.springframework.data.jpa.domain.Specification;

public class MenuitemSpecification {
    public static Specification<Menuitem> hasName(String name) {
        return (root, query, criteriaBuilder) ->
                name == null ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }
}
