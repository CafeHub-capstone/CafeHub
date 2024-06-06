package com.cafehub.cafehub.comment.repository;

import com.cafehub.cafehub.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findAllByMemberId(Pageable pageable, Long memberId);

    Slice<Comment> findAllByReviewId(Pageable pageable, Long reviewId);

    List<Comment> findAllByReviewId(Long reviewId);

    // 해당 comment 엔티티와 연관된 member 엔티티 페치 조인으로 함께 로드.
    @Query("SELECT c FROM Comment c JOIN FETCH c.member WHERE c.review.id = :reviewId")
    Slice<Comment> findAllByReviewIdWithMember(@Param("reviewId") Long reviewId, Pageable pageable);

    void deleteAllByReviewId(Long reviewId);
}
