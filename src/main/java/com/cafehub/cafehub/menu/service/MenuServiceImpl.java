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

    @Override
    public AllMenuResponse getAllMenu(AllMenuRequest allMenuRequest){

        List<Menu> menuList = menuRepository.findAllByCafeId(allMenuRequest.getCafeId());
        List<MenuResponse> menuResponseList = new ArrayList<>();

        for(Menu menu : menuList){

            MenuResponse menuResponse = new MenuResponse(menu.getCategory(),menu.getName(),menu.getPrice());
            menuResponseList.add(menuResponse);
        }

        return new AllMenuResponse(true, menuResponseList,"ok");
    }

}
