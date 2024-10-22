package com.jemo.RestaurantReviewPortal.restaurant;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.jemo.RestaurantReviewPortal.menu.Menu;
import com.jemo.RestaurantReviewPortal.review.Review;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Restaurant {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    @NonNull
    @NotEmpty(message = "name cannot be empty")
    private String name;

    @Column(nullable = false)
    @NonNull
    @NotEmpty(message = "address cannot be empty")
    private String address;

    @Column(nullable = false)
    @NonNull
    @NotEmpty(message = "phone cannot be empty")
    private String phone;

    @Email
    @NonNull
    @NotEmpty(message = "email cannot be empty")
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @NonNull
    @NotEmpty(message = "city cannot be empty")
    private String city;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "restaurant")
    private List<Menu> menus;

    @OneToMany(mappedBy = "restaurant")
    private List<Review> reviews;

    // private RestaurantCategory restaurantCategory;   // work on this later

    @Nullable
    private Double averageRating;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

}
