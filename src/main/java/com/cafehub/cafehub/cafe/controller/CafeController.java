package com.cafehub.cafehub.cafe.controller;


import com.cafehub.cafehub.cafe.request.CafeInfoRequestDTO;
import com.cafehub.cafehub.cafe.request.CafeListGetRequestDTO;
import com.cafehub.cafehub.cafe.service.CafeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CafeController {

    private final CafeService cafeService;

    @GetMapping("/cafeList/{theme}/{sortedByType}/{currentPage}")
    public ResponseEntity <?> getAllCafe(@PathVariable("theme") String theme,
                                         @PathVariable("sortedByType") String sortedByType,
                                         @PathVariable("currentPage") Integer currentPage){

        CafeListGetRequestDTO request = new CafeListGetRequestDTO(theme, sortedByType, currentPage);

        return ResponseEntity.ok().body(cafeService.getCafeListSortedByType(request));
    }


    @GetMapping("/cafe/{cafeId}")
    public ResponseEntity<?> getCafeInfo(@PathVariable("cafeId") Long cafeId){

        CafeInfoRequestDTO request = new CafeInfoRequestDTO(cafeId);

        return ResponseEntity.ok().body(cafeService.getCafeInfo(request));
    }


}
