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
public class CafeListResponseDTO {

    private List<CafeDetailsDTO> cafeList;

    private Boolean isLast;

    private Integer currentPage;

}
