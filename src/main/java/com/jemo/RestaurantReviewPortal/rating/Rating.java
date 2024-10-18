package com.jemo.RestaurantReviewPortal.rating;

import com.jemo.RestaurantReviewPortal.review.Review;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.lang.NonNull;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rating {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name = "review_id")
    private Review review;

    @Column(nullable = false)
    @NonNull
    private Double foodQuality;

    @Column(nullable = false)
    @NonNull
    private Double customerService;

    @Column(nullable = false)
    @NonNull
    private Double cleanlinessAndHygiene;

    @Column(nullable = false)
    @NonNull
    private Double ambiance;

    @Column(nullable = false)
    @NonNull
    private Double valueForMoney;

    @Column(nullable = false)
    @NonNull
    private Double overallRating;
}
