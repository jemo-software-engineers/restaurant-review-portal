package com.jemo.RestaurantReviewPortal.menu;

import com.jemo.RestaurantReviewPortal.restaurant.Restaurant;
import com.jemo.RestaurantReviewPortal.restaurant.RestaurantRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;

    public List<Menu> findAllMenus() {
        return menuRepository.findAll();
    }

    public Menu findMenuByRestaurantIdAndMenuId(Long restaurantId, Long Id) {
        return menuRepository.findByRestaurantIdAndId(restaurantId, Id);
    }

    public List<Menu> findAllMenuByRestaurant(Long restaurantId) {
        return menuRepository.findAllByRestaurantId(restaurantId);
    }

    @Transactional
    public Boolean deleteById(Long restaurantId, Long menuId) {
        Menu menu = findMenuByRestaurantIdAndMenuId(restaurantId, menuId);
        if(menu != null) {
            menuRepository.delete(menu);
        }

        // confirm deletion
        if(findMenuByRestaurantIdAndMenuId(restaurantId, menuId) == null) {
            return true;
        }
        return false;
    }

    @Transactional
    public Boolean updateById(Long restaurantId, Long menuId, MenuRequest menuRequest) {
        Menu menu = findMenuByRestaurantIdAndMenuId(restaurantId, menuId);
        if(menu.getId() != null) {
            Menu updatedMenu = Menu.builder()
                    .id(menu.getId())
                    .restaurantId(menu.getRestaurantId())
                    .description(menuRequest.description() != null ? menuRequest.description() : menu.getDescription())
                    .name(menuRequest.name() != null ? menuRequest.name() : menu.getName())
                    .build();
            menuRepository.save(updatedMenu);
            return true;
        }
        return false;
    }

    @Transactional
    public Boolean createMenu(Long restaurantId, MenuRequest menuRequest) {
        // check if menu exists
        Menu menuExists = menuRepository.findByRestaurantIdAndName(restaurantId, menuRequest.name());
        if(menuExists != null) {
            return false;
        }
        // check if restaurant exists
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);
        if(restaurant == null) {
            return false;
        }
        // create menu
        Menu newMenu = Menu.builder()
                 .restaurantId(restaurantId)
                 .name(menuRequest.name())
                 .description(menuRequest.description())
                 .build();
        Menu savedMenu = menuRepository.save(newMenu);
        if(savedMenu.getId() != null) {
            return true;
        }
        return false;
    }
}
