package com.cafehub.cafehub.theme.entity;

import com.cafehub.cafehub.common.dto.Timestamped;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Theme extends Timestamped {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "theme_id")
    private Long id;

    private String name;

//    @OneToMany(mappedBy = "theme")
//    private List<Cafe> cafes = new ArrayList<>();

}
