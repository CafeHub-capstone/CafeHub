package com.cafehub.cafehub.review.repository;

import com.cafehub.cafehub.review.entity.Review;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.cafehub.cafehub.likeReview.entity.QLikeReview.likeReview;
import static com.cafehub.cafehub.review.entity.QReview.review;

@RequiredArgsConstructor
public class ReviewRepositoryCustomImpl implements ReviewRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;



    @Override
    public List<Review> findBestReviewFetch(Long cafeId){

        return jpaQueryFactory.selectFrom(review)
                .leftJoin(likeReview)
                .on(review.eq(likeReview.review))
                .where(review.cafe.id.eq(cafeId))
                .groupBy(review)
                .orderBy(likeReview.count().desc())
                .limit(2)
                .fetch();
    }
}
