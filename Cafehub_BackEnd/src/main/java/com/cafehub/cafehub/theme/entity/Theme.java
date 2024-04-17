package com.cafehub.cafehub.theme.entity;

import com.cafehub.cafehub.cafe.entity.Cafe;
import com.cafehub.cafehub.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Theme extends BaseEntity{

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "theme_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "theme")
    private List<Cafe> cafes = new ArrayList<>();

}
