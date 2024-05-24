package com.cafehub.cafehub.review.repository;

import com.cafehub.cafehub.review.entity.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByMemberId(Long memberId);

    Slice<Review> findAllByCafeId(Pageable pageable, Long cafeId);
}
