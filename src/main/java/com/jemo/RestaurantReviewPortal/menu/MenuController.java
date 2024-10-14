package com.jemo.RestaurantReviewPortal.menu;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MenuController {
    private final MenuService menuService;

    // retrieve all menus
    @GetMapping("/api/menus")
    public ResponseEntity<List<MenuResponse>> getAllMenus() {
        List<Menu> menuList = menuService.findAllMenus();
        return getMenuResponseList(menuList);
    }

    // retrieve a particular menu
    @GetMapping("/api/restaurants/{restaurantId}/menus/{menuId}")
    public ResponseEntity<MenuResponse> getMenuById(@PathVariable Long restaurantId, @PathVariable Long menuId) {
        Menu menuItem = menuService.findMenuByRestaurantIdAndMenuId(restaurantId, menuId);
        if (menuItem == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        MenuResponse menuResponse = new MenuResponse();
        menuResponse.setId(menuItem.getId());
        menuResponse.setName(menuItem.getName());
        menuResponse.setDescription(menuItem.getDescription());
        menuResponse.setRestaurantId(menuItem.getRestaurantId());
        return new ResponseEntity<>(menuResponse, HttpStatus.OK);
    }

    // retrieve all menus for a particular restaurant
    @GetMapping("/api/restaurants/{restaurantId}/menus")
    public ResponseEntity<List<MenuResponse>> getAllMenusForSingleRestaurant(@PathVariable Long restaurantId) {
        List<Menu> menuList = menuService.findAllMenuByRestaurant(restaurantId);
        if(menuList == null || menuList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return getMenuResponseList(menuList);
    }

    // create a new menu for a restaurant
    @PostMapping("/admin/api/restaurants/{restaurantId}/menus")
    public ResponseEntity<String> createMenu(@PathVariable Long restaurantId, @Valid @RequestBody MenuRequest menuRequest) {
        Boolean created = menuService.createMenu(restaurantId, menuRequest);
        if (created) {
            return new ResponseEntity<>("Menu has been created", HttpStatus.OK);
        }
        return new ResponseEntity<>("Menu could not been created", HttpStatus.BAD_REQUEST);

    }

    // update a particular menu
    @PutMapping("/admin/api/restaurants/{restaurantId}/menus/{menuId}")
    public ResponseEntity<String> updateMenu(@Valid @RequestBody MenuRequest menuRequest, @PathVariable Long restaurantId, @PathVariable Long menuId) {
        Boolean updated = menuService.updateById(restaurantId, menuId, menuRequest);
        if (updated) {
            return new ResponseEntity<>("Menu " + menuId + " has been updated", HttpStatus.OK);
        }
        return new ResponseEntity<>("Menu " + menuId + " could not been updated", HttpStatus.BAD_REQUEST);
    }

    // delete a particular menu
    @DeleteMapping("/admin/api/restaurants/{restaurantId}/menus/{menuId}")
    public ResponseEntity<String> deleteMenu(@PathVariable Long restaurantId, @PathVariable Long menuId) {
        Boolean deleted = menuService.deleteById(restaurantId, menuId);
        if (deleted) {
            return new ResponseEntity<>("Menu " + menuId + " has been deleted", HttpStatus.OK);
        }
       return new ResponseEntity<>("Menu " + menuId + " could not been deleted", HttpStatus.BAD_REQUEST);
    }



    // private method to convert list of menus to MenuResponse format
    private ResponseEntity<List<MenuResponse>> getMenuResponseList(List<Menu> menuList) {
        List<MenuResponse> menuResponseList = menuList.stream()
                .map(menu -> {
                    MenuResponse menuResponse = new MenuResponse();
                    menuResponse.setId(menu.getId());
                    menuResponse.setName(menu.getName());
                    menuResponse.setDescription(menu.getDescription());
                    menuResponse.setRestaurantId(menu.getRestaurantId());
                    return menuResponse;
                }).toList();
        return new ResponseEntity<>(menuResponseList, HttpStatus.OK);
    }


}
