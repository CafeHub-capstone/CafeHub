package com.cafehub.cafehub.review.repository;

import com.cafehub.cafehub.review.entity.Review;

import java.util.List;

public interface ReviewRepositoryCustom {
    List<Review> findBestReviewFetch(Long cafeId);
}
