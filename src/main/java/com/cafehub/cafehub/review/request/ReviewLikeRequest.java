package com.cafehub.cafehub.review.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewLikeRequest {

    private Long reviewId;

    private Boolean reviewChecked;
}
