package com.cafehub.cafehub.menu.controller;

import com.cafehub.cafehub.menu.request.AllMenuRequest;
import com.cafehub.cafehub.menu.response.AllMenuResponse;
import com.cafehub.cafehub.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@CrossOrigin
@RestController
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;


    @GetMapping("/cafe/{cafeId}/menu")
    public AllMenuResponse getAllMenu(@PathVariable("cafeId") Long cafeId){

        AllMenuRequest allMenuRequest = new AllMenuRequest(cafeId);

        return menuService.getAllMenu(allMenuRequest);
    }


}
