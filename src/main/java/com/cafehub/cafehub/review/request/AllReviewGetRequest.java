package com.cafehub.cafehub.review.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AllReviewGetRequest {
    // API Path : /cafe/{cafeId}/reviews/{currentPage}

    private Long cafeId;

    private Integer currentPage;
}