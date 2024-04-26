package com.cafehub.cafehub.cafe.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CafeResponseForCafeList {

    private Long cafeId;

    private String cafePhotoUrl;

    private String cafeName;

    private BigDecimal cafeRating;

    private String cafeTheme;

    private Integer cafeReviewNum;
}
