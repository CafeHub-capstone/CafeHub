package com.cafehub.cafehub.cafe.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CafeDetailsDTO {

    private Long cafeId;

    private String cafePhotoUrl;

    private String cafeName;

    private String cafeTheme;

    /**
     * BigDecimal : 정밀도가 중요하고 소수점 이하 자리가 필요한 경우, 메모리를 많이 쓰고 연산이 느림
     * double     : 약간의 부동소수점 오차가 허용되고, 성능이 중요한 경우
     * float      : 아주 낮은 정밀도가 필요하고, 메모리 사용량을 최소화하려는 경우
     */

    private BigDecimal cafeRating;

    private Integer cafeReviewNum;
}
