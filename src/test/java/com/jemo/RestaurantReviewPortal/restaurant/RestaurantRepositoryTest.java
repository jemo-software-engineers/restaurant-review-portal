package com.jemo.RestaurantReviewPortal.restaurant;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

//import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class RestaurantRepositoryTest {
    @Autowired
    private RestaurantRepository restaurantRepository;
    private Restaurant restaurant;

    @AfterEach
    void tearDown() {
        restaurantRepository.deleteAll();
    }

    @Test
    void itShouldFindByEmail() {
        // given
        String email = "zikichips100@gmail.com";

        Restaurant newRestaurant = Restaurant.builder()
                .website("restaurant.com")
                .email("zikichips100@gmail.com")
                .phone("118229387")
                .city("Stoke")
                .cuisine(RestaurantCuisine.AFRICAN)
                .address("Kiln House")
                .name("KFC")
                .build();
        restaurantRepository.save(newRestaurant);

        // when
        Restaurant theRestaurant = restaurantRepository.findByEmail(email);
        // then
        Assertions.assertEquals("zikichips100@gmail.com", theRestaurant.getEmail());
    }

    @Test
    void itShouldFindByName() {
        // given
        String name = "KFC";

        Restaurant newRestaurant = Restaurant.builder()
                .website("restaurant.com")
                .email("zikichips100@gmail.com")
                .phone("118229387")
                .city("Stoke")
                .cuisine(RestaurantCuisine.AFRICAN)
                .address("Kiln House")
                .name("KFC")
                .build();
        restaurantRepository.save(newRestaurant);

        // when
        Restaurant theRestaurant = restaurantRepository.findByName(name);
        // then
        Assertions.assertEquals("KFC", theRestaurant.getName());
    }

}