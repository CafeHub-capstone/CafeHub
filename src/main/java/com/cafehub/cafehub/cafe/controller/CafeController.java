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

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "CAFE", description = "CAFE 관련 API 테스트")
public class CafeController {

    private final CafeService cafeService;



    @Operation(summary = "카페 리스트업", description = "테마, 정렬기준으로 카페 리스트업을 반환합니다.")
    @Parameters({
            @Parameter(name = "theme", description = "All | Study | Meet | Date | Dessert"),
            @Parameter(name = "sortedByType", description = "name | reviewNum | rating"),
            @Parameter(name = "currentPage", description = "Default : 0 (First page)")
        })
    @GetMapping("/cafeList/{theme}/{sortedByType}/{currentPage}")
    public ResponseEntity <?> getAllCafe(@PathVariable("theme") String theme,
                                         @PathVariable("sortedByType") String sortedByType,
                                         @PathVariable("currentPage") Integer currentPage){

        CafeListGetRequestDTO request = new CafeListGetRequestDTO(theme, sortedByType, currentPage);

        return ResponseEntity.ok().body(cafeService.getCafeListSortedByType(request));
    }


    
    @Operation(summary = "카페 상세페이지" , description = "CafeId로 해당 카페 상세 페이지 정보 반환")
    @Parameter(name = "cafeId", description = "CafeId : 1 | 2 | ...")
    @GetMapping("/cafe/{cafeId}")
    public ResponseEntity<?> getCafeInfo(@PathVariable("cafeId") Long cafeId){

        CafeInfoRequestDTO request = new CafeInfoRequestDTO(cafeId);

        return ResponseEntity.ok().body(cafeService.getCafeInfo(request));
    }


}
