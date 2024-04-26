package com.cafehub.cafehub.cafe.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CafeListResponse {

    private Boolean success;

    private List<CafeResponseForCafeList> cafeList;

    private Boolean isLast;

    private Integer currentPage;

    private String errorMessage;
}
