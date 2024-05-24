package com.cafehub.cafehub.menu.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BestMenuResponse {

    private Long menuId;

    private String name;

    private Integer price;
}
