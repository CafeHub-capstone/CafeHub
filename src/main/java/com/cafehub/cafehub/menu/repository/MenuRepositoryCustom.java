package com.cafehub.cafehub.menu.repository;

import com.cafehub.cafehub.menu.entity.Menu;

import java.util.List;

public interface MenuRepositoryCustom {
    List<Menu> findBestMenuFetch(Long cafeId);
}
