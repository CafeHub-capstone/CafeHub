package com.cafehub.cafehub.cafe.controller;


import com.cafehub.cafehub.cafe.request.CafeInfoRequestDTO;
import com.cafehub.cafehub.cafe.request.CafeListGetRequestDTO;
import com.cafehub.cafehub.cafe.service.CafeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
