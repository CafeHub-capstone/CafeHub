package com.cafehub.cafehub.menu.response;

import com.cafehub.cafehub.menu.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MenuResponse {

    private Category category;

    private String name;

    private Integer price;
}
