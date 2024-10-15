package com.jemo.RestaurantReviewPortal.menuitem;

import com.jemo.RestaurantReviewPortal.menu.Menu;
import com.jemo.RestaurantReviewPortal.review.Review;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Menuitem {
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

    @Column(nullable = false)
    @NonNull
    private BigDecimal price;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @Column(nullable = false)
    @NonNull
    @Enumerated(EnumType.STRING)
    private Availability availability;

    @Column(nullable = false)
    @NonNull
    @NotEmpty(message = "dietary info cannot be empty")
    private String dietaryInfo;

    @OneToMany(mappedBy = "menuitem")
    private List<Review> reviews;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
