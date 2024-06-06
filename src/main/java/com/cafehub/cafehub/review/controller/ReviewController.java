package com.cafehub.cafehub.review.controller;

import com.cafehub.cafehub.review.request.AllReviewGetRequest;
import com.cafehub.cafehub.review.request.ReviewCreateRequest;
import com.cafehub.cafehub.review.request.ReviewDeleteRequest;
import com.cafehub.cafehub.review.request.ReviewUpdateRequest;
import com.cafehub.cafehub.review.response.AllReviewGetResponse;
import com.cafehub.cafehub.review.response.ReviewCreateResponse;
import com.cafehub.cafehub.review.response.ReviewDeleteResponse;
import com.cafehub.cafehub.review.response.ReviewUpdateResponse;
import com.cafehub.cafehub.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    @PostMapping(value = "/api/auth/cafe/{cafeId}/review", consumes = {"multipart/form-data"})
    public ReviewCreateResponse createReview(
            @PathVariable("cafeId") Long cafeId,
            @RequestPart("ReviewCreateRequest") ReviewCreateRequest request,
            @RequestPart(value = "photos", required = false) List<MultipartFile> photos) {

        request.setCafeId(cafeId);

        return reviewService.writeReview(request, photos);
    }



    // 리뷰 수정.
    @PostMapping(value = "/api/auth/cafe/{reviewId}/update", consumes = {"multipart/form-data"})
    public ReviewUpdateResponse updateReview(@PathVariable("reviewId") Long reviewId,
                                             @RequestPart("request")  ReviewUpdateRequest reviewUpdateRequest,
                                             @RequestPart(value = "photos", required = false) List<MultipartFile> photos){

        reviewUpdateRequest.setReviewId(reviewId);

        return reviewService.updateReview(reviewUpdateRequest,photos);
    }

    // 리뷰 삭제.
    @PostMapping("/api/auth/cafe/{reviewId}/delete")
    public ReviewDeleteResponse deleteReview(@PathVariable("reviewId") Long reviewId){

        ReviewDeleteRequest reviewDeleteRequest = new ReviewDeleteRequest(reviewId);

        return reviewService.deleteReview(reviewDeleteRequest);
    }
}
