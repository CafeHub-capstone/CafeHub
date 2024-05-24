package com.cafehub.cafehub.bookmark.response;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkResponseDto {

    private boolean success;
    private Long cafeId;
    private String errorMessage;

}