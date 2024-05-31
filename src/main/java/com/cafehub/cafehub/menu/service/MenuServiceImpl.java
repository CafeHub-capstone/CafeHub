package com.cafehub.cafehub.menu.service;


import com.cafehub.cafehub.menu.entity.Menu;
import com.cafehub.cafehub.menu.repository.MenuRepository;
import com.cafehub.cafehub.menu.request.AllMenuGetRequestDTO;
import com.cafehub.cafehub.menu.response.AllMenuResponseDTO;
import com.cafehub.cafehub.menu.response.MenuResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService{

    private final MenuRepository menuRepository;


    /**
     *  메뉴 리스트는 페이징 요청이 없어서 리스트로 넘겨줌
     *  추후 메뉴 사진도 추가하게 되면 페이징 처리하는게 좋을듯, 현재는 메뉴의 text만 넘겨줌
     *
     *  DB와 통신 횟수 : 1
     */
    @Override
    public AllMenuResponseDTO getAllMenuByCafeId(AllMenuGetRequestDTO request){

        List<Menu> menus = menuRepository.findAllByCafeId(request.getCafeId());

        List<MenuResponseDTO> menuList = menus.stream()
                .map(menu -> new MenuResponseDTO(
                        menu.getId(),
                        menu.getCategory(),
                        menu.getName(),
                        menu.getPrice()
                ))
                .toList();

        return new AllMenuResponseDTO(menuList);
    }

}
