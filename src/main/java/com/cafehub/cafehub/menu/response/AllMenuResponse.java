package com.cafehub.cafehub.menu.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AllMenuResponse {

    private Boolean success;

    private List<MenuResponse> menuList;

    private String errorMessage;
}

