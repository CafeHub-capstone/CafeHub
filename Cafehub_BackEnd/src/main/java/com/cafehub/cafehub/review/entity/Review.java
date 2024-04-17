package com.cafehub.cafehub.review.entity;

import com.cafehub.cafehub.cafe.entity.Cafe;
import com.cafehub.cafehub.comment.entity.Comment;
import com.cafehub.cafehub.common.entity.BaseEntity;
import com.cafehub.cafehub.likeReview.entity.LikeReview;
import com.cafehub.cafehub.member.entity.Member;
import com.cafehub.cafehub.reviewPhoto.entity.ReviewPhoto;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Review extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "review_id")
    private Long id;

    private Integer rating;

    @Lob
    private String content;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafe_id")
    private Cafe cafe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;


    @OneToMany(mappedBy = "review")
    private List<ReviewPhoto> reviewPhotos = new ArrayList<>();

    @OneToMany(mappedBy = "review")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "review")
    private List<LikeReview> likeReviews = new ArrayList<>();




}
