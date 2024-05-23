package com.cafehub.cafehub.cafe.response;

import com.cafehub.cafehub.menu.response.BestMenuResponse;
import com.cafehub.cafehub.menu.response.MenuResponse;
import com.cafehub.cafehub.review.response.BestReviewResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CafeInfoResponse {

    private Boolean success;

    private Long cafeId;

    private String cafePhotoUrl;

    private String cafeName;

    private String cafeTheme;

    // 리뷰 변경에 영향을 받음
    private Integer cafeReviewCnt;

    private String cafeOperationHour;

    private String cafeAddress;

    private String cafePhone;

    // 리뷰 변경에 영향을 받음
    private BigDecimal cafeRating;

    private Boolean bookmarkChecked;

    private List<BestMenuResponse> bestMenuList;

    private List<BestReviewResponse> bestReviewList;

    private String errorMessage;

}
