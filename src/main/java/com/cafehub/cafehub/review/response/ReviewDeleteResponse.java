package com.cafehub.cafehub.review.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDeleteResponse {

    private Boolean success;

    private Long reviewId;

    private String errorMessage;
}
