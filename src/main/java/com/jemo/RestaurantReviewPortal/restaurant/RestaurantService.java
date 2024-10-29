package com.jemo.RestaurantReviewPortal.restaurant;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;

    @Transactional
    public boolean createRestaurant(RestaurantRequest restaurantRequest) {
        Restaurant restaurantExists = restaurantRepository.findByEmail(restaurantRequest.email());
        if (restaurantExists != null) {
            return false;
        }

        Restaurant restaurantToCreate = Restaurant.builder()
                .name(restaurantRequest.name())
                .address(restaurantRequest.address())
                .city(restaurantRequest.city())
                .phone(restaurantRequest.phone())
                .email(restaurantRequest.email())
                .website(restaurantRequest.website())
                .cuisine(RestaurantCuisine.valueOf(restaurantRequest.cuisine()))
                .build();
        Restaurant newRestaurant = restaurantRepository.save(restaurantToCreate);
        return (newRestaurant.getId() != null) ? true : false;
    }

    public Restaurant findById(long id) {
        return restaurantRepository.findById(id).orElse(null);
    }

    public Restaurant findByName(String name) {
        Restaurant theRestaurant = restaurantRepository.findByName(name);
        return theRestaurant != null ? theRestaurant : null;
    }

    public List<Restaurant> findAll() {
        return restaurantRepository.findAll();
    }

    public List<Restaurant> searchRestaurants(String name, String city, String cuisine) {
        Specification<Restaurant> spec = Specification.where(RestaurantSpecification.hasName(name))
                .and(RestaurantSpecification.hasCity(city))
                .and(RestaurantSpecification.hasCuisine(cuisine));
        return restaurantRepository.findAll(spec);
    }

    @Transactional
    public Boolean deleteById(long id) {
        Restaurant restaurant = findById(id);
        if (restaurant != null) {
            restaurantRepository.delete(restaurant);
            return true;
        }
        return false;
    }

    @Transactional
    public Boolean updateById(long id, RestaurantRequest restaurantRequest) {
        Restaurant restaurant = findById(id);
        if(restaurant != null) {
            restaurant.setId(id);
            restaurant.setName(restaurantRequest.name() != null ? restaurantRequest.name() : restaurant.getName());
            restaurant.setCity(restaurantRequest.city() != null ? restaurantRequest.city() : restaurant.getCity());
            restaurant.setEmail(restaurantRequest.email() != null ? restaurantRequest.email() : restaurant.getEmail());
            restaurant.setAddress(restaurantRequest.address() != null ? restaurantRequest.address() : restaurant.getAddress());
            restaurant.setPhone(restaurantRequest.phone() != null ? restaurantRequest.phone() : restaurant.getPhone());
            restaurant.setWebsite(restaurantRequest.website() != null ? restaurantRequest.website() : restaurant.getWebsite());
            restaurant.setCuisine(restaurantRequest.cuisine() != null ? RestaurantCuisine.valueOf(restaurantRequest.cuisine()) : restaurant.getCuisine());
            restaurantRepository.save(restaurant);
            return true;
        }
        return false;
    }

    public Boolean updateRestaurantRating(Long restaurantId, Double averageRating) {
        Restaurant restaurant = findById(restaurantId);
        if(restaurant != null) {
            restaurant.setAverageRating(averageRating);
            restaurantRepository.save(restaurant);
            return true;
        }
        return false;
    }
}
