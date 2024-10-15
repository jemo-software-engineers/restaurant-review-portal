package com.jemo.RestaurantReviewPortal.menuitem;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuitemRepository extends JpaRepository<Menuitem, Long> {
    Menuitem findByMenuIdAndId(Long menuId, Long id);
    List<Menuitem> findAllByMenuId(Long menuId);
    Menuitem findByMenuIdAndName(Long menuId, String name);
}
