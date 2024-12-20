package com.jemo.RestaurantReviewPortal.restaurant;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class RestaurantController {
    private final RestaurantService restaurantService;




    // create restaurant - admin
    @PostMapping(value = "/admin/api/restaurants", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createRestaurant(
            @RequestPart("restaurantRequest") String restaurantRequestJson,
            @RequestPart(value = "file", required = false) MultipartFile file) throws JsonProcessingException {
        // Convert JSON string to RestaurantRequest object
        RestaurantRequest restaurantRequest = new ObjectMapper()
                .readValue(restaurantRequestJson, RestaurantRequest.class);
        Restaurant duplicateRestaurant = restaurantService.findByName(restaurantRequest.name());
        if(duplicateRestaurant != null) {
            return new ResponseEntity<>("Restaurant Creation Failed - Restaurant already exists", HttpStatus.BAD_REQUEST);
        }
        boolean restaurantCreated = restaurantService.createRestaurant(restaurantRequest, file);
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
        restaurantResponse.setWebsite(restaurantRetrieved.getWebsite());
        restaurantResponse.setImageUrl(restaurantRetrieved.getImageUrl());
        restaurantResponse.setCuisine(String.valueOf(restaurantRetrieved.getCuisine()));

        return new ResponseEntity<>(restaurantResponse, HttpStatus.OK);
    }

    // get list of all restaurants - anybody
    @GetMapping("/api/restaurants")
    public ResponseEntity<List<RestaurantResponse>> getAllRestaurants(@Nullable @RequestParam String name, @Nullable @RequestParam String city, @Nullable @RequestParam String cuisine) {
        List<Restaurant> restaurantList;
        if(name != null || city != null || cuisine != null) {
            restaurantList = restaurantService.searchRestaurants(name, city, cuisine);
        } else {
            restaurantList = restaurantService.findAll();
        }
        return convertListOfRestaurantsToRestaurantResponse(restaurantList);
    }

    @GetMapping("/api/cuisine")
    public ResponseEntity<List<RestaurantCuisine>> getAllCuisine() {
        return new ResponseEntity(Arrays.asList(RestaurantCuisine.values()), HttpStatus.OK);
    }

    @GetMapping("/api/cities")
    public ResponseEntity<List<RestaurantCities>> getAllCities() {
        return new ResponseEntity(Arrays.asList(RestaurantCities.values()), HttpStatus.OK);
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
                    restaurantResponse.setWebsite(restaurant.getWebsite());
                    restaurantResponse.setImageUrl(restaurant.getImageUrl());
                    restaurantResponse.setCuisine(String.valueOf(restaurant.getCuisine()));
                    return restaurantResponse;
                }).toList();

        return new ResponseEntity<>(allRestaurenats, HttpStatus.OK);
    }


    // update restaurant - admin
    @PutMapping(value ="/admin/api/restaurants/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateRestaurant(
            @PathVariable long id,
            @RequestPart("restaurantRequest") String restaurantRequestJson,
            @RequestPart(value = "file", required = false) MultipartFile file) throws JsonProcessingException {
        // Convert JSON string to RestaurantRequest object
        RestaurantRequest restaurantRequest = new ObjectMapper()
                .readValue(restaurantRequestJson, RestaurantRequest.class);

        Boolean updated = (file == null) ? restaurantService.updateById(id, restaurantRequest) : restaurantService.updateById(id, restaurantRequest, file);
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
