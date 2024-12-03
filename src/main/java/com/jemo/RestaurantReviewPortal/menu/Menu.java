package com.jemo.RestaurantReviewPortal.menu;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.jemo.RestaurantReviewPortal.menuitem.Menuitem;
import com.jemo.RestaurantReviewPortal.restaurant.Restaurant;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Menu {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    @NonNull
    @NotEmpty(message = "name cannot be empty")
    private String name;

    @Column(nullable = false)
    @NonNull
    @NotEmpty(message = "description cannot be empty")
    private String description;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "menu")
    private List<Menuitem> menuitems;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
