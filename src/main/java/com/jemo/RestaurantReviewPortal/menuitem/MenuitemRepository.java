package com.jemo.RestaurantReviewPortal.menuitem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface MenuitemRepository extends JpaRepository<Menuitem, Long>, JpaSpecificationExecutor<Menuitem> {
    Menuitem findByMenuIdAndId(Long menuId, Long id);
    List<Menuitem> findAllByMenuId(Long menuId);
    Menuitem findByMenuIdAndName(Long menuId, String name);
}
