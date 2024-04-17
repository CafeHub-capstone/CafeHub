package com.cafehub.cafehub.cafe.entity;

import com.cafehub.cafehub.bookmark.entity.Bookmark;
import com.cafehub.cafehub.common.entity.BaseEntity;
import com.cafehub.cafehub.menu.entity.Menu;
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
public class Cafe extends BaseEntity {

    @Id
    @Column (name = "cafe_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String address;

    private String latitude;

    private String longtitude;

    @Lob
    private String cafePhotoUrl;

    private String phone;

    private BigDecimal rating;

    private String reviewCount;

    private String operationHours;

    private String closedDays;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id")
    private Theme theme;

    @OneToMany(mappedBy = "cafe")
    private List<Menu> menus = new ArrayList<>();

    @OneToMany(mappedBy = "cafe")
    private List<Bookmark> bookmarks = new ArrayList<>();

    @OneToMany(mappedBy = "cafe")
    private List<Review> reviews = new ArrayList<>();





}
