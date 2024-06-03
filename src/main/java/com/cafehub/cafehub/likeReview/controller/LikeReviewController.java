package com.cafehub.cafehub.likeReview.controller;

import com.cafehub.cafehub.likeReview.dto.LikeReviewRequestDto;
import com.cafehub.cafehub.likeReview.service.LikeReviewService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LikeReviewController {

    private final LikeReviewService likeReviewService;

    @PostMapping("/api/auth/cafe/{reviewId}/like")
    public ResponseEntity<?> updateLike(@PathVariable Long reviewId, @RequestBody LikeReviewRequestDto likeReviewRequestDto, HttpServletRequest request) {
        return ResponseEntity.ok().body(likeReviewService.updateLike(reviewId, likeReviewRequestDto, request));
    }
}
