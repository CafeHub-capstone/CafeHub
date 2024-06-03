package com.cafehub.cafehub.reviewPhoto.entity;

import com.cafehub.cafehub.common.dto.Timestamped;
import com.cafehub.cafehub.review.entity.Review;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewPhoto extends Timestamped {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "review_photo_id")
    private Long id;

    @Lob
    private String reviewPhotoUrl;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;


}
