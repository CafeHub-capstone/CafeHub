package com.cafehub.cafehub.menu.service;


import com.cafehub.cafehub.menu.entity.Menu;
import com.cafehub.cafehub.menu.repository.MenuRepository;
import com.cafehub.cafehub.menu.request.AllMenuRequest;
import com.cafehub.cafehub.menu.response.AllMenuResponse;
import com.cafehub.cafehub.menu.response.MenuResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService{

    private final MenuRepository menuRepository;

    // 전체 메뉴를 가져오기 , DB와 소통 횟수 : 1번
    @Override
    public AllMenuResponse getAllMenu(AllMenuRequest allMenuRequest){

        List<Menu> menuList = menuRepository.findAllByCafeId(allMenuRequest.getCafeId());

        List<MenuResponse> menuResponseList = menuList.stream()
                .map(menu -> new MenuResponse(
                        menu.getId(),
                        menu.getCategory().name(),
                        menu.getName(),
                        menu.getPrice()
                ))
                .toList();

        return new AllMenuResponse(true, menuResponseList,"ok");
    }

}
