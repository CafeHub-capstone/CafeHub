package com.cafehub.cafehub.cafe.controller;


import com.cafehub.cafehub.cafe.request.CafeInfoRequest;
import com.cafehub.cafehub.cafe.request.CafeListRequest;
import com.cafehub.cafehub.cafe.response.CafeInfoResponse;
import com.cafehub.cafehub.cafe.response.CafeListResponse;
import com.cafehub.cafehub.cafe.service.CafeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin
public class CafeController {

    private final CafeService cafeService;

    @GetMapping("/api/cafeList/{theme}/{sortedByType}/{currentPage}")
    public CafeListResponse cafeList(@PathVariable("theme") String theme,
                                     @PathVariable("sortedByType") String sortedByType,
                                     @PathVariable("currentPage") Integer currentPage){

        // 프론트 개발자 측 스크롤 구현 관련 요청으로 화면 랜더링시 버그로 currnetPage가 -1 이 한번 들어오고 시작하는 오류가 발생함
        // 그래서 -1 요청시 아무것도 반환하지 않는 코드 추가해달라고 함
        if(currentPage==-1) return new CafeListResponse(true,null,false,currentPage,"currentPage:-1");

        CafeListRequest cafeListRequest = new CafeListRequest(theme, sortedByType, currentPage);


        // (리팩토링 포인트) 이 방식이 옳을까 ?
        // response를 만들어 준 후 각각의 기능들을 담당하는 서비스들로 데이터를 받아온 후 세팅하는 것
        // vs 해당 방식처럼 한번에 서비스 하나로 처리하기
        // + DTO를 이렇게 바로 반환시키는게 맞을까? , 분명 추상화 시킬수 있을거 같음
        // 근데 또 의문점이 Service 에서 Controller로 데이터를 불러와서 던져도 DTO에 담아서 주려면 또 새로운 DTO를 만들어야 하는거 아닌가? 싶음

        return cafeService.getCafeListResponseSortedByType(cafeListRequest);
    }



    @GetMapping("/api/cafe/{cafeId}")
    public CafeInfoResponse cafeInfo(@PathVariable("cafeId") Long cafeId){

        CafeInfoRequest cafeInfoRequest = new CafeInfoRequest(cafeId);

        return cafeService.getCafeInfo(cafeInfoRequest);
    }






}
