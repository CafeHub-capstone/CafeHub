package com.cafehub.cafehub.cafe.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CafeListRequest {

    // Date, Dessert, Meeting, Study, All
    private String theme;

    // name : 이름순 (가나다)
    // rating : 별점 높은 순
    // reviewNum : 리뷰 개수 많은 순
    private String sortedByType;

    // 0 | 1 | 2 ..
    private Integer currentPage;
}
