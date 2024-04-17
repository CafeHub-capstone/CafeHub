package com.cafehub.cafehub.reviewPhoto.entity;

import com.cafehub.cafehub.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewPhoto extends BaseEntity{

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "review_photo_id")
    private Long id;

    @Lob
    private String reviewPhotoUrl;
}
