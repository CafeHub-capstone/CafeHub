package com.cafehub.cafehub.review.controller;

import com.cafehub.cafehub.review.request.*;
import com.cafehub.cafehub.review.response.*;
import com.cafehub.cafehub.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin // 원산지 표기같은 거임.
@Slf4j // 테스트용 로거.
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "전체 리뷰리스트 반환", description = "CafeId에 해당하는 카페의 전체 리뷰를 반환합니다.")
    @Parameters({
            @Parameter(name = "cafeId", description = "1 | 2 | 3 . . ."),
            @Parameter(name = "currentPage", description = "Default : 0 (First page)")
    })
    @GetMapping("/api/cafe/{cafeId}/reviews/{currentPage}")
    public AllReviewGetResponse allReviewGet(@PathVariable("cafeId") Long cafeId,
                                             @PathVariable("currentPage") Integer currentPage){

        return reviewService.getAllReviewOfCafe(new AllReviewGetRequest(cafeId, currentPage));
    }


    @Operation(summary = "리뷰 작성", description = "cafeId에 해당하는 카페에 리뷰를 생성합니다.")
    @Parameters({
            @Parameter(name = "cafeId", description = "1 | 2 | 3 . . ."),
            @Parameter(name = "reviewRating", description = "1 | 2 ... |5 , Integer"),
            @Parameter(name = "photoUrls", description = "리뷰의 사진파일 리스트"),
            @Parameter(name = "reviewContent", description = "리뷰의 내용 text")
    })
    @PostMapping("/api/auth/cafe/{cafeId}/review")
    public ReviewCreateResponse createReview(@PathVariable("cafeId") Long cafeId,
                                             @RequestBody ReviewCreateRequest reviewCreateRequest){

        reviewCreateRequest.setCafeId(cafeId);

        return reviewService.writeReview(reviewCreateRequest);
    }

    // 리뷰 수정.
    @PostMapping("/api/auth/cafe/{reviewId}/update")
    public ReviewUpdateResponse updateReview(@PathVariable("reviewId") Long reviewId,
                                             @RequestBody ReviewUpdateRequest reviewUpdateRequest){

        reviewUpdateRequest.setReviewId(reviewId);

        return reviewService.updateReview(reviewUpdateRequest);
    }

    // 리뷰 삭제.
    @PostMapping("/api/auth/cafe/{reviewId}/delete")
    public ReviewDeleteResponse deleteReview(@PathVariable("reviewId") Long reviewId){

        ReviewDeleteRequest reviewDeleteRequest = new ReviewDeleteRequest(reviewId);

        return reviewService.deleteReview(reviewDeleteRequest);
    }
}
