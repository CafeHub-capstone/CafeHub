package com.cafehub.cafehub.review.controller;

import com.cafehub.cafehub.review.request.*;
import com.cafehub.cafehub.review.response.*;
import com.cafehub.cafehub.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin // 원산지 표기같은 거임.
@Slf4j // 테스트용 로거.
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰 전체보기.
    @GetMapping("/api/cafe/{cafeId}/reviews/{currentPage}")
    public AllReviewGetResponse allReviewGet(@PathVariable("cafeId") Long cafeId,
                                             @PathVariable("currentPage") Integer currentPage){

        return reviewService.getAllReviewOfCafe(new AllReviewGetRequest(cafeId, currentPage));
    }

    // 리뷰 작성.
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
