package com.jemo.RestaurantReviewPortal.review;

import com.jemo.RestaurantReviewPortal.menuitem.Menuitem;
import com.jemo.RestaurantReviewPortal.rating.Rating;
import com.jemo.RestaurantReviewPortal.restaurant.Restaurant;
import com.jemo.RestaurantReviewPortal.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Review {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    @NonNull
    @NotEmpty(message = "title cannot be empty")
    private String reviewTitle;

    @Column(nullable = false)
    @NonNull
    @NotEmpty(message = "text cannot be empty")
    private String reviewText;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @Nullable
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @ManyToOne
    @Nullable
    @JoinColumn(name = "menuitem_id")
    private Menuitem menuitem;

    @OneToOne(mappedBy = "review", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Rating rating;

    @Column(nullable = false)
    @NonNull
    @Enumerated(EnumType.STRING)
    private ReviewStatus status;

    // review approved by
    @ManyToOne
    @Nullable
    @JoinColumn(name = "approved_by_admin_id")
    private User approved_by;


    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
