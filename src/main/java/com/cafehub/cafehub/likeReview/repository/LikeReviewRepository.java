package com.cafehub.cafehub.likeReview.repository;

import com.cafehub.cafehub.likeReview.entity.LikeReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeReviewRepository extends JpaRepository<LikeReview, Long> {
    List<LikeReview> findByMemberIdAndReviewIdIn(Long memberId, List<Long> reviewIds);
}
