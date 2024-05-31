package com.cafehub.cafehub.bookmark.response;

import lombok.*;

import java.math.BigDecimal;
@Setter
@Builder
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BookmarkedCafeDetails {

    private Long cafeId;

    private String cafePhotoUrl;

    private String cafeName;

    private BigDecimal cafeRating;

    private String cafeTheme;

    private Integer cafeReviewNum;

}
