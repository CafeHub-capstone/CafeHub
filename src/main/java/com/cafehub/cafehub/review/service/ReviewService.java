package com.cafehub.cafehub.review.service;

import com.cafehub.cafehub.review.request.*;
import com.cafehub.cafehub.review.response.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ReviewService {

    AllReviewGetResponse getAllReviewOfCafe(AllReviewGetRequest allReviewGetRequest);

    @Transactional
    ReviewCreateResponse writeReview(ReviewCreateRequest reviewCreateRequest, List<MultipartFile> photos);

    @Transactional
    ReviewUpdateResponse updateReview(ReviewUpdateRequest reviewUpdateRequest, List<MultipartFile> photos);

    @Transactional
    ReviewDeleteResponse deleteReview(ReviewDeleteRequest reviewDeleteRequest);

}
