package com.jemo.RestaurantReviewPortal.menu;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

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

    @Column(nullable = false)
    @NonNull
    @NotEmpty(message = "name cannot be empty")
    private String name;

    @Column(nullable = false)
    @NonNull
    @NotEmpty(message = "description cannot be empty")
    private String description;

    private Long restaurantId;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
