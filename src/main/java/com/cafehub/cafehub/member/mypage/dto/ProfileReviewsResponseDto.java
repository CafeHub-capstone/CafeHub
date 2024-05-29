package com.cafehub.cafehub.member.mypage.dto;

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

    private List<ReviewResponseDto> reviewList;

    private Boolean isLast;

    private Integer currentPage;
}
