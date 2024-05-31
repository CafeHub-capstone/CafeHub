package com.cafehub.cafehub.review.response;

import com.cafehub.cafehub.reviewPhoto.response.PhotoUrlResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponse {

    private Long reviewId;

    private String author;

    private Integer reviewRating;

    private String reviewContent;

    private String reviewCreateDate;

    private Integer likeCnt;

    private Boolean likeChecked; // 내가 리뷰 좋아요 눌렀는지.

    private Integer commentCnt;

    private List<PhotoUrlResponse> photoUrls;

    private Boolean reviewManagement; // 로그인 인가 작업 필요.
}
