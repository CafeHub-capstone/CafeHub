package com.cafehub.cafehub.cafe.controller;


import com.cafehub.cafehub.cafe.request.CafeListRequest;
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


    @GetMapping("/cafeList/{theme}/{sortedByType}/{currentPage}")
    public CafeListResponse cafeList(@PathVariable("theme") String theme,
                                     @PathVariable("sortedByType") String sortedByType,
                                     @PathVariable("currentPage") Integer currentPage){

        CafeListRequest cafeListRequest = new CafeListRequest(theme, sortedByType, currentPage);

        return cafeService.getCafeListResponseSortedByType(cafeListRequest);
    }




}
