package com.cafehub.cafehub.comment.repository;

import com.cafehub.cafehub.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findAllByMemberId(Pageable pageable, Long memberId);

    Slice<Comment> findAllByReviewId(Pageable pageable, Long reviewId);
}
