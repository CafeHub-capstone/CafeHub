package com.cafehub.cafehub.likeReview.repository;

import com.cafehub.cafehub.likeReview.entity.LikeReview;
import com.cafehub.cafehub.member.entity.Member;
import com.cafehub.cafehub.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeReviewRepository extends JpaRepository<LikeReview, Long> {
    Optional<LikeReview> findByReviewAndMember(Review review, Member member);
    Boolean existsByMemberAndReview(Member member, Review review);
    List<LikeReview> findAllByMemberId(Long memberId);
    List<LikeReview> findByMemberIdAndReviewIdIn(Long memberId, List<Long> reviewIds);
}
