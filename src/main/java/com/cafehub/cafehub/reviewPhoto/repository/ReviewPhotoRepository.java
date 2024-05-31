package com.cafehub.cafehub.reviewPhoto.repository;

import com.cafehub.cafehub.reviewPhoto.entity.ReviewPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewPhotoRepository extends JpaRepository<ReviewPhoto, Long> {

    List<ReviewPhoto> findAllByReviewId(Long ReviewId);

    void deleteAllByReviewId(Long reviewId);

    List<ReviewPhoto> findAllByReviewIdIn(List<Long> reviewIds);
}
