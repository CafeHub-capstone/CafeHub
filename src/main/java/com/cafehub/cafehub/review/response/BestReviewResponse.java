package com.cafehub.cafehub.review.response;

import com.cafehub.cafehub.reviewPhoto.response.PhotoUrlResponse;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BestReviewResponse {

    private Long reviewId;

    private String author;

    private Integer reviewRating;

    private String reviewContent;

    private LocalDateTime reviewDate;

    private Integer likeCnt;

    private Integer commentCnt;

    private Boolean likeChecked;;

    private List<PhotoUrlResponse> photoUrls;

}