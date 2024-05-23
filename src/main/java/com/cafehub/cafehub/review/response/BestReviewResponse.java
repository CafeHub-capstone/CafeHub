package com.cafehub.cafehub.review.response;

import com.cafehub.cafehub.reviewPhoto.response.PhotoUrlResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BestReviewResponse {

    private Long reviewId;

    private String author;

    private Integer reviewRating;

    private String reviewContent;

    private LocalDateTime reviewDate;

    private Integer likeCnt;

    private Integer commentCnt;

    private Boolean reviewChecked;

    private List<PhotoUrlResponse> photoUrls;

}
