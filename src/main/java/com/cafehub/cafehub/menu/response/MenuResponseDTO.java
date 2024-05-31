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
public class MenuResponseDTO {

    private Long menuId;

    private Category category;

    private String menuName;

    private Integer price;
}
