package com.jemo.RestaurantReviewPortal.menuitem;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MenuitemController {
    private final MenuitemService menuitemService;

    // retrieve all menuitems
    @GetMapping("/api/menuitems")
    public ResponseEntity<List<MenuitemResponse>> getAllMenuitems() {
        List<Menuitem> menuitemList = menuitemService.findAllMenuitems();
        return getMenuitemResponseList(menuitemList);
    }

    // retrieve a particular menuitem
    @GetMapping("/api/menus/{menuId}/menuitems/{menuitemId}")
    public ResponseEntity<MenuitemResponse> getMenuitemById(@PathVariable Long menuId, @PathVariable Long menuitemId) {
        Menuitem menuItem = menuitemService.findMenuitemByMenuIdAndMenuitemId(menuId, menuitemId);
        if (menuItem == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        MenuitemResponse menuitemResponse = new MenuitemResponse();
        menuitemResponse.setId(menuItem.getId());
        menuitemResponse.setName(menuItem.getName());
        menuitemResponse.setDescription(menuItem.getDescription());
        menuitemResponse.setMenuId(menuItem.getMenu().getId());
        menuitemResponse.setMenuName(menuItem.getMenu().getName());
        menuitemResponse.setPrice(menuItem.getPrice());
        menuitemResponse.setDietaryInfo(menuItem.getDietaryInfo());
        menuitemResponse.setAvailability(menuItem.getAvailability());
        menuitemResponse.setAverageRating(menuItem.getAverageRating());
        return new ResponseEntity<>(menuitemResponse, HttpStatus.OK);
    }


    // search menuitem by name
    @GetMapping("/api/menuitems/search")
    public ResponseEntity<List<MenuitemResponse>> getMenuitemByName(@Nullable @RequestParam String name){
        List<Menuitem> menuitemList = menuitemService.searchMenuitem(name);

        return getMenuitemResponseList(menuitemList);
    }

    // retrieve all menuitems for a particular menu
    @GetMapping("/api/menus/{menuId}/menuitems")
    public ResponseEntity<List<MenuitemResponse>> getAllMenuitemsForSingleMenu(@PathVariable Long menuId) {
        List<Menuitem> menuitemList = menuitemService.findAllMenuitemsByMenu(menuId);
        if(menuitemList == null || menuitemList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return getMenuitemResponseList(menuitemList);
    }


    // create a new menuitem for a menu
    @PostMapping("/admin/api/menus/{menuId}/menuitems")
    public ResponseEntity<String> createMenuitem(@PathVariable Long menuId, @Valid @RequestBody MenuitemRequest menuitemRequest) {
        Boolean created = menuitemService.createMenuitem(menuId, menuitemRequest);
        if (created) {
            return new ResponseEntity<>("Menu has been created", HttpStatus.OK);
        }
        return new ResponseEntity<>("Menu could not been created", HttpStatus.BAD_REQUEST);

    }

    // update a particular menuitem
    @PutMapping("/admin/api/menus/{menuId}/menuitems/{menuitemId}")
    public ResponseEntity<String> updateMenuitem(@Valid @RequestBody MenuitemRequest menuitemRequest, @PathVariable Long menuId, @PathVariable Long menuitemId) {
        Boolean updated = menuitemService.updateById(menuId, menuitemId, menuitemRequest);
        if (updated) {
            return new ResponseEntity<>("Menuitem " + menuitemId + " has been updated", HttpStatus.OK);
        }
        return new ResponseEntity<>("Menuitem " + menuitemId + " could not been updated", HttpStatus.BAD_REQUEST);
    }

    // delete a particular menuitem
    @DeleteMapping("/admin/api/menus/{menuId}/menuitems/{menuitemId}")
    public ResponseEntity<String> deleteMenuitem(@PathVariable Long menuitemId, @PathVariable Long menuId) {
        Boolean deleted = menuitemService.deleteById(menuId, menuitemId);
        if (deleted) {
            return new ResponseEntity<>("Menuitem " + menuitemId + " has been deleted", HttpStatus.OK);
        }
        return new ResponseEntity<>("Menuitem " + menuId + " could not been deleted", HttpStatus.BAD_REQUEST);
    }


    // private method to convert list of menuitems to MenuitemResponse format
    private ResponseEntity<List<MenuitemResponse>> getMenuitemResponseList(List<Menuitem> menuitemList) {
        List<MenuitemResponse> menuitemResponseList = menuitemList.stream()
                .map(menuitem -> {
                    MenuitemResponse menuitemResponse = new MenuitemResponse();
                    menuitemResponse.setId(menuitem.getId());
                    menuitemResponse.setName(menuitem.getName());
                    menuitemResponse.setDescription(menuitem.getDescription());
                    menuitemResponse.setAvailability(menuitem.getAvailability());
                    menuitemResponse.setPrice(menuitem.getPrice());
                    menuitemResponse.setDietaryInfo(menuitem.getDietaryInfo());
                    menuitemResponse.setMenuId(menuitem.getMenu().getId());
                    menuitemResponse.setMenuName(menuitem.getMenu().getName());
                    menuitemResponse.setAverageRating(menuitem.getAverageRating());
                    return menuitemResponse;
                }).toList();
        return new ResponseEntity<>(menuitemResponseList, HttpStatus.OK);
    }
}
