package com.jemo.RestaurantReviewPortal.restaurant;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RestaurantController {
    private final RestaurantService restaurantService;

    // create restaurant - admin
    @PostMapping("/admin/api/restaurants")
    public ResponseEntity<String> createRestaurant(@Valid @RequestBody RestaurantRequest restaurantRequest) {
        Restaurant duplicateRestaurant = restaurantService.findByName(restaurantRequest.name());
        if(duplicateRestaurant != null) {
            return new ResponseEntity<>("Restaurant Creation Failed - Restaurant already exists", HttpStatus.BAD_REQUEST);
        }
        boolean restaurantCreated = restaurantService.createRestaurant(restaurantRequest);
        if(restaurantCreated) {
            return new ResponseEntity<>("Restaurant Created Successfully", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Restaurant Creation Failed", HttpStatus.BAD_REQUEST);
    }

    // get single restaurant - anybody
    @GetMapping("/api/restaurants/{id}")
    public ResponseEntity<RestaurantResponse> getRestaurantById(@PathVariable long id) {
        Restaurant restaurantRetrieved = restaurantService.findById(id);
        if(restaurantRetrieved == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        RestaurantResponse restaurantResponse = new RestaurantResponse();

        restaurantResponse.setRestaurantId(restaurantRetrieved.getId());
        restaurantResponse.setRestaurantName(restaurantRetrieved.getName());
        restaurantResponse.setRestaurantAddress(restaurantRetrieved.getAddress());
        restaurantResponse.setRestaurantCity(restaurantRetrieved.getCity());
        restaurantResponse.setRestaurantEmail(restaurantRetrieved.getEmail());
        restaurantResponse.setRestaurantPhone(restaurantRetrieved.getPhone());
        restaurantResponse.setAverageRating(restaurantRetrieved.getAverageRating());

        return new ResponseEntity<>(restaurantResponse, HttpStatus.OK);
    }

    // get list of all restaurants - anybody
    @GetMapping("/api/restaurants")
    public ResponseEntity<List<RestaurantResponse>> getAllRestaurants() {
        List<Restaurant> restaurantList = restaurantService.findAll();

        return convertListOfRestaurantsToRestaurantResponse(restaurantList);
    }

    // search restaurants by name and city
    @GetMapping("/api/restaurants/search")
    public ResponseEntity<List<RestaurantResponse>> getRestaurantByNameAndCity(@Nullable @RequestParam String name, @Nullable @RequestParam String city){
        List<Restaurant> restaurantList = restaurantService.searchRestaurants(name, city);

        return convertListOfRestaurantsToRestaurantResponse(restaurantList);
    }

    private ResponseEntity<List<RestaurantResponse>> convertListOfRestaurantsToRestaurantResponse(List<Restaurant> restaurantList) {
        List<RestaurantResponse> allRestaurenats = restaurantList.stream()
                .map(restaurant -> {
                    RestaurantResponse restaurantResponse = new RestaurantResponse();
                    restaurantResponse.setRestaurantId(restaurant.getId());
                    restaurantResponse.setRestaurantName(restaurant.getName());
                    restaurantResponse.setRestaurantAddress(restaurant.getAddress());
                    restaurantResponse.setRestaurantCity(restaurant.getCity());
                    restaurantResponse.setRestaurantEmail(restaurant.getEmail());
                    restaurantResponse.setRestaurantPhone(restaurant.getPhone());
                    restaurantResponse.setAverageRating(restaurant.getAverageRating());
                    return restaurantResponse;
                }).toList();

        return new ResponseEntity<>(allRestaurenats, HttpStatus.OK);
    }

    // update restaurant - admin
    @PutMapping("/admin/api/restaurants/{id}")
    public ResponseEntity<String> updateRestaurant(@PathVariable long id, @Valid @RequestBody RestaurantRequest restaurantRequest) {
        Boolean updated = restaurantService.updateById(id, restaurantRequest);
        if(updated) {
            return new ResponseEntity<>("Restaurant Updated Successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Restaurant Update Failed", HttpStatus.BAD_REQUEST);
    }

    // delete restaurant - admin
    @DeleteMapping("/admin/api/restaurants/{id}")
    public ResponseEntity<String> deleteRestaurant(@PathVariable long id) {
        Boolean deleted = restaurantService.deleteById(id);
        if(deleted) {
            return new ResponseEntity<>("Restaurant Deleted Successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Restaurant Deletion Failed", HttpStatus.BAD_REQUEST);
    }
}
