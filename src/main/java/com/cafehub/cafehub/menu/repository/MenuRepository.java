package com.cafehub.cafehub.menu.repository;

import com.cafehub.cafehub.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long>, MenuRepositoryCustom {

    List<Menu> findAllByCafeId(Long cafeId);

}
