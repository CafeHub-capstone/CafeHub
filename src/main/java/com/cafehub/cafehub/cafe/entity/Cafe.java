package com.cafehub.cafehub.cafe.entity;

import com.cafehub.cafehub.common.dto.Timestamped;
import com.cafehub.cafehub.review.entity.Review;
import com.cafehub.cafehub.theme.entity.Theme;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Cafe extends Timestamped {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "cafe_id")
    private Long id;

    private String name;

    private String address;

    // AWS S3와 관련된 필드
    @Lob
    private String cafePhotoUrl;

    private String phone;

    // 리뷰의 변경에 영향을 받는 필드
    private BigDecimal rating;

    // 리뷰의 변경에 영향을 받는 필드
    private Integer reviewCount;

    private String operationHours;

    private String closedDays;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id")
    private Theme theme;


    /**
     *   필요할 시 팀원들과 회의 후에 다같이 결정하기로 함
     *
     *   @OneToMany(mappedBy = "cafe")
     *   private List<Menu> menus = new ArrayList<>();
     *
     *   @OneToMany(mappedBy = "cafe")
     *   private List<Bookmark> bookmarks = new ArrayList<>();
     */


    @OneToMany(mappedBy = "cafe")
    @Builder.Default
    private List<Review> reviews = new ArrayList<>();

    public void updateRating(BigDecimal rating) {
        this.rating = rating;
    }

    public void updateReviewCount(Integer reviewCount) {
        this.reviewCount = reviewCount;
    }
    
}
