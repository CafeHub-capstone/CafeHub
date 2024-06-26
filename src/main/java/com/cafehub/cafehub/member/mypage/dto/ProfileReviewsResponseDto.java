package com.cafehub.cafehub.member.mypage.dto;

import com.cafehub.cafehub.review.response.ReviewResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileReviewsResponseDto {

    private List<ReviewResponse> reviewList;

    private Boolean isLast;

    private Integer currentPage;
}
