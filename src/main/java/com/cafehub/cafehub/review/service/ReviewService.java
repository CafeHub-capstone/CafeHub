package com.cafehub.cafehub.review.service;

import com.cafehub.cafehub.review.request.*;
import com.cafehub.cafehub.review.response.*;
import org.springframework.transaction.annotation.Transactional;

public interface ReviewService {

    AllReviewGetResponse getAllReviewOfCafe(AllReviewGetRequest allReviewGetRequest);

    @Transactional
    ReviewCreateResponse writeReview(ReviewCreateRequest reviewCreateRequest);

    @Transactional
    ReviewUpdateResponse updateReview(ReviewUpdateRequest reviewUpdateRequest);

    @Transactional
    ReviewDeleteResponse deleteReview(ReviewDeleteRequest reviewDeleteRequest);

}
