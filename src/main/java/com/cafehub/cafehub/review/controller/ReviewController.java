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
@Slf4j // 테스트용 로거.
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/api/cafe/{cafeId}/reviews/{currentPage}")
    public AllReviewGetResponse allReviewGet(@PathVariable("cafeId") Long cafeId,
                                             @PathVariable("currentPage") Integer currentPage){

        return reviewService.getAllReviewOfCafe(new AllReviewGetRequest(cafeId, currentPage));
    }

    // 리뷰 사진이라고 올린 파일이 png, jpg 파일이 맞는지 확인해야 하는데
    @PostMapping(value = "/api/auth/cafe/{cafeId}/review", consumes = "multipart/form-data")
    public ReviewCreateResponse createReview(@PathVariable("cafeId") Long cafeId,
                                             @RequestBody ReviewCreateRequest reviewCreateRequest){

        reviewCreateRequest.setCafeId(cafeId);

        return reviewService.writeReview(reviewCreateRequest);
    }

    // 리뷰 수정.
    @PostMapping(value = "/api/auth/cafe/{reviewId}/update", consumes = "multipart/form-data")
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
