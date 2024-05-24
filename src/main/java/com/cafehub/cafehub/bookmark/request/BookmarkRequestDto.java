package com.cafehub.cafehub.bookmark.request;

import lombok.*;
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkRequestDto {

  private boolean bookmarkChecked;
  private Long cafeId;

}
