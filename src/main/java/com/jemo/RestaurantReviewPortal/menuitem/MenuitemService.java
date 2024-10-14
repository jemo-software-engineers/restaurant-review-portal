package com.jemo.RestaurantReviewPortal.menuitem;

import com.jemo.RestaurantReviewPortal.menu.Menu;
import com.jemo.RestaurantReviewPortal.menu.MenuRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuitemService {
    private final MenuitemRepository menuitemRepository;
    private final MenuRepository menuRepository;

    public List<Menuitem> findAllMenuitems() {
        return menuitemRepository.findAll();
    }

    public Menuitem findMenuitemByMenuIdAndMenuitemId(Long menuId, Long menuitemId) {
        return menuitemRepository.findByMenuIdAndId(menuId, menuitemId);
    }

    public List<Menuitem> findAllMenuitemsByMenu(Long menuId) {
        return menuitemRepository.findAllByMenuId(menuId);
    }

    @Transactional
    public Boolean createMenuitem(Long menuId, @Valid MenuitemRequest menuitemRequest) {
        // check if menuItem exists
        Menuitem menuitemCheck = menuitemRepository.findByMenuIdAndName(menuId, menuitemRequest.name());
        if (menuitemCheck != null) {
            return false;
        }

        // check if menu exists
        Menu menuCheck = menuRepository.findById(menuId).orElse(null);
        if (menuCheck == null) {
            return false;
        }

        // create the new menuitem
        Menuitem newMenuitem = Menuitem.builder()
                .name(menuitemRequest.name())
                .description(menuitemRequest.description())
                .price(menuitemRequest.price())
                .dietaryInfo(menuitemRequest.dietaryInfo())
                .menuId(menuId)
                .availability(menuitemRequest.availability())
                .build();
        Menuitem savedMenuitem = menuitemRepository.save(newMenuitem);
        if(savedMenuitem.getId() != null) {
            return true;
        }
        return false;
    }

    @Transactional
    public Boolean deleteById(Long menuId, Long menuitemId) {
        Menuitem menuitem = findMenuitemByMenuIdAndMenuitemId(menuId, menuitemId);
        if(menuitem != null) {
            menuitemRepository.delete(menuitem);
        }

        // confirm deletion
        if(findMenuitemByMenuIdAndMenuitemId(menuId, menuitemId) == null) {
            return true;
        }
        return false;
    }

    @Transactional
    public Boolean updateById(Long menuId, Long menuitemId, @Valid MenuitemRequest menuitemRequest) {
        Menuitem menuitem = findMenuitemByMenuIdAndMenuitemId(menuId, menuitemId);
        if(menuitem.getId() != null) {
            Menuitem updatedMenuitem = Menuitem.builder()
                    .id(menuitem.getId())
                    .name(menuitemRequest.name() != null ? menuitemRequest.name() : menuitem.getName())
                    .description(menuitemRequest.description() != null ? menuitemRequest.description() : menuitem.getDescription())
                    .price(menuitemRequest.price() != null ? menuitemRequest.price() : menuitem.getPrice())
                    .availability(menuitemRequest.availability() != null ? menuitemRequest.availability() : menuitem.getAvailability())
                    .menuId(menuId)
                    .dietaryInfo(menuitemRequest.dietaryInfo() != null ? menuitemRequest.dietaryInfo() : menuitem.getDietaryInfo())
                    .build();
            menuitemRepository.save(updatedMenuitem);
            return true;
        }
        return false;
    }
}
