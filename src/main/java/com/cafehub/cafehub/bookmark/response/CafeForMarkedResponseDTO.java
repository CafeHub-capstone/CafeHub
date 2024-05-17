package com.cafehub.cafehub.bookmark.response;

import lombok.*;
import java.math.BigDecimal;
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CafeForMarkedResponseDTO {

    private Long cafeId;

    private String cafePhotoUrl;

    private String cafeName;

    private BigDecimal cafeRating;

    private String cafeTheme;

    private Integer cafeReviewNum;

}
