package com.cafehub.cafehub.cafe.response;

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

    private String cafePhotoUrl;

    private String cafename;

    private String cafeOperationHour;

    private String cafeAddress;

    private String cafePhone;

    private BigDecimal cafeRating;

    private List<MenuResponse> bestMenuList;

    private List<BestReviewResponse> bestReviewList;

    private String errorMessage;

}
