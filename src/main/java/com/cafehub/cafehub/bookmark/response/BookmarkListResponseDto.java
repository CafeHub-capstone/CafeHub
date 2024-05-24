package com.cafehub.cafehub.bookmark.response;

import lombok.*;
import java.util.List;
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkListResponseDto {

    private boolean success;
    private List<CafeForMarkedResponseDTO> cafeList;
    private String errorMessage;
}
