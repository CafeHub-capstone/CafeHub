package com.cafehub.cafehub.review.repository;

import com.cafehub.cafehub.review.entity.Review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findAllByMemberId(Pageable pageable, Long memberId);

    Slice<Review> findAllByCafeId(Pageable pageable, Long cafeId);

    // cafe 에서 필요함
    @Query("SELECT DISTINCT r FROM Review r JOIN FETCH r.member LEFT JOIN FETCH r.reviewPhotos WHERE r.cafe.id = :cafeId")
    List<Review> findAllByCafeIdWithMemberAndPhotos(@Param("cafeId") Long cafeId, Pageable pageable);
}
