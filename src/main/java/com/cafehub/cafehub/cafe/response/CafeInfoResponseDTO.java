package com.cafehub.cafehub.cafe.response;

import com.cafehub.cafehub.menu.response.BestMenuResponseDTO;
import com.cafehub.cafehub.review.response.BestReviewResponse;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CafeInfoResponseDTO {

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

    // 현재 로그인한 사용자가 해당 카페를 북마크에 추가한 적이 있는지
    private Boolean bookmarkChecked;

    private List<BestMenuResponseDTO> bestMenuList;
    
    private List<BestReviewResponse> bestReviewList;

}
