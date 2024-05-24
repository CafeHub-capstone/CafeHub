package com.cafehub.cafehub.menu.request;

import com.cafehub.cafehub.menu.response.MenuResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AllMenuRequest {


    private Long cafeId;
}