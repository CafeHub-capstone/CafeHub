package com.cafehub.cafehub.review.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter // 예외처리 감안해서.
@AllArgsConstructor
@NoArgsConstructor
public class AllReviewGetResponse {

    private Boolean success;

    private List<ReviewResponse> reviewList;

    private BigDecimal cafeRating;

    private Integer cafeReviewCnt;

    private Boolean isLast;

    private Integer currentPage;

    private String errorMessage;
}
