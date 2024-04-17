package com.cafehub.cafehub.member.entity;

import com.cafehub.cafehub.bookmark.entity.Bookmark;
import com.cafehub.cafehub.comment.entity.Comment;
import com.cafehub.cafehub.common.entity.BaseEntity;
import com.cafehub.cafehub.likeReview.entity.LikeReview;
import com.cafehub.cafehub.review.entity.Review;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Member extends BaseEntity{

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String email;

    private String nickname;

    @Lob
    private String userPhotoUrl;

    @Enumerated(EnumType.STRING)
    private Role role;


    @OneToMany(mappedBy = "member")
    private List<Bookmark> bookmarks = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<LikeReview> likeReviews = new ArrayList<>();

}
