package com.jemo.RestaurantReviewPortal.restaurant;

import com.jemo.RestaurantReviewPortal.awsconfig.S3ImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {


    private RestaurantService restaurantService;
    private S3ImageService s3ImageService;
    @Mock  private RestaurantRepository restaurantRepository;

    @BeforeEach
    void setUp() {
        restaurantService = new RestaurantService(restaurantRepository, s3ImageService);
    }

    @Test
    @Disabled
    void canCreateRestaurant() {
        // given
        RestaurantRequest newRestaurant = new RestaurantRequest(
                "KFC",
                "Kiln House",
                "118229387",
                "zikichips100@gmail.com",
                "Stoke",
                "restaurant.com",
                "AFRICAN"

        );


        // when
//        restaurantService.createRestaurant(newRestaurant);
    }

    @Test
    @Disabled
    void findById() {
        // when
    }

    @Test
    @Disabled
    void findByName() {
    }

    @Test
    void canFindAll() {
        // when
        restaurantService.findAll();
        // then
        verify(restaurantRepository).findAll();
    }

    @Test
    @Disabled
    void searchRestaurants() {
    }

    @Test
    @Disabled
    void deleteById() {
    }

    @Test
    @Disabled
    void updateById() {
    }

    @Test
    @Disabled
    void testUpdateById() {
    }

    @Test
    @Disabled
    void updateRestaurantRating() {
    }

    @Test
    @Disabled
    void uploadRestaurantImage() {
    }
}