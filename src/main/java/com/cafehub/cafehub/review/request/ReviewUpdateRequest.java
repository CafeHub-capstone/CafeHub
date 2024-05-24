package com.cafehub.cafehub.review.request;

import com.cafehub.cafehub.reviewPhoto.request.PhotoUrlRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewUpdateRequest {

    private Long reviewId;

    private Integer reviewRating;

    private List<PhotoUrlRequest> photoUrls;

    private String reviewContent;
}
