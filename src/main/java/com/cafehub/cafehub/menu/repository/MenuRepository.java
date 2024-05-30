package com.cafehub.cafehub.menu.repository;

import com.cafehub.cafehub.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long>{

    List<Menu> findAllByCafeId(Long cafeId);

    List<Menu> findAllByCafeIdAndBestTrue(Long cafeId);
}
