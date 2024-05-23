package com.cafehub.cafehub.menu.repository;

import com.cafehub.cafehub.menu.entity.Menu;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.cafehub.cafehub.menu.entity.QMenu.menu;

@RequiredArgsConstructor
public class MenuRepositoryCustomImpl implements MenuRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public List<Menu> findBestMenuFetch(Long cafeId){

        return  jpaQueryFactory.selectFrom(menu)
                .where(menu.cafe.id.eq(cafeId))
                .where(menu.best.eq(true))
                .fetch();
    }
}
