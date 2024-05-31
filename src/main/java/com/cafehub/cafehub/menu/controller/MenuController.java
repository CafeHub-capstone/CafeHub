package com.cafehub.cafehub.menu.controller;


import com.cafehub.cafehub.common.dto.ResponseDto;
import com.cafehub.cafehub.menu.request.AllMenuGetRequestDTO;
import com.cafehub.cafehub.menu.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;


    @Operation(summary = "메뉴 리스트업", description = "CafeId에 해당하는 카페의 전체 메뉴를 반환합니다.")
    @Parameter(name = "cafeId", description = "1 | 2 | 3 ...")
    @GetMapping("/api/cafe/{cafeId}/menu")
    public ResponseEntity<?> getAllMenu(@PathVariable("cafeId") Long cafeId){

        AllMenuGetRequestDTO request = new AllMenuGetRequestDTO(cafeId);

        return ResponseEntity.ok().body(ResponseDto.success(menuService.getAllMenuByCafeId(request)));
    }


}
