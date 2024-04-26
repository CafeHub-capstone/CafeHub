package com.cafehub.cafehub.menu.entity;

import com.cafehub.cafehub.cafe.entity.Cafe;
import com.cafehub.cafehub.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Menu extends BaseEntity{

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private Category category;

    private String name;

    private Integer price;

    private Boolean best;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafe_id")
    private Cafe cafe;



}
