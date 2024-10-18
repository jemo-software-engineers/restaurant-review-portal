package com.jemo.RestaurantReviewPortal.rating;

import com.jemo.RestaurantReviewPortal.review.Review;
import com.jemo.RestaurantReviewPortal.review.ReviewRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RatingService {
    private final RatingRepository ratingRepository;

    public Rating addRatingToReview(Review review, ReviewRequest reviewRequest) {
        Rating newRating = Rating.builder()
                .review(review)
                .ambiance(reviewRequest.ambiance())
                .customerService(reviewRequest.customerService())
                .cleanlinessAndHygiene(reviewRequest.cleanlinessAndHygiene())
                .foodQuality(reviewRequest.foodQuality())
                .valueForMoney(reviewRequest.valueForMoney())
                .overallRating((reviewRequest.ambiance()
                        + reviewRequest.customerService()
                        + reviewRequest.cleanlinessAndHygiene()
                        + reviewRequest.foodQuality()
                        + reviewRequest.valueForMoney()) / 5)
                .build();
        return ratingRepository.save(newRating);
    }

    public Boolean updateRatingForReview(Review review, @Valid ReviewRequest reviewRequest) {
        Rating newRating = Rating.builder()
                .review(review)
                .id(review.getRating().getId())
                .ambiance(reviewRequest.ambiance() != null ? reviewRequest.ambiance() : review.getRating().getAmbiance())
                .customerService(reviewRequest.customerService() != null ? reviewRequest.customerService() : review.getRating().getCustomerService())
                .cleanlinessAndHygiene(reviewRequest.cleanlinessAndHygiene() != null ? reviewRequest.cleanlinessAndHygiene() : review.getRating().getCleanlinessAndHygiene())
                .foodQuality(reviewRequest.foodQuality() != null ? reviewRequest.foodQuality() : review.getRating().getFoodQuality())
                .valueForMoney(reviewRequest.valueForMoney() != null ? reviewRequest.valueForMoney() : review.getRating().getValueForMoney())
                .overallRating(
                        ( // recalculate rating based on most recent values
                                (reviewRequest.ambiance() != null ? reviewRequest.ambiance() : review.getRating().getAmbiance())
                        + (reviewRequest.customerService() != null ? reviewRequest.customerService() : review.getRating().getCustomerService())
                        + (reviewRequest.cleanlinessAndHygiene() != null ? reviewRequest.cleanlinessAndHygiene() : review.getRating().getCleanlinessAndHygiene())
                        + (reviewRequest.foodQuality() != null ? reviewRequest.foodQuality() : review.getRating().getFoodQuality())
                        + (reviewRequest.valueForMoney() != null ? reviewRequest.valueForMoney() : review.getRating().getValueForMoney())
                        )
                        / 5)
                .build();
        ratingRepository.save(newRating);
        return true;
    }
}
