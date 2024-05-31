package com.cafehub.cafehub.bookmark.controller;

import com.cafehub.cafehub.bookmark.request.BookmarkRequestDto;
import com.cafehub.cafehub.bookmark.response.BookmarkListResponseDto;
import com.cafehub.cafehub.bookmark.response.BookmarkResponseDto;
import com.cafehub.cafehub.bookmark.service.BookmarkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "BOOKMARK", description = "BOOKMARK 관련 API 테스트")
public class BookmarkController {

    private final BookmarkService bookmarkService;


    @Operation(summary = "북마크 리스트업", description = "현재 로그인한 사용자의 북마크 리스트를 반환합니다.")
    @GetMapping("/bookmarks")
    public BookmarkListResponseDto bookmarkList(){
       return bookmarkService.getBookmarkList();
    }

    @Operation(summary = "북마크 생성, 삭제", description = "북마크 체크여부를 기반으로 북마크를 생성하거나 삭제합니다.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody
//    @Parameters({
//            @Parameter(name = "cafeId", description = "1 | 2 | 3 ..."),
//            @Parameter(name = "bookmarkChecked", description = "true(생성) | false(삭제)")
//    })
    @PostMapping("/bookmark")
    public BookmarkResponseDto bookmarkSaveOrDelete(@RequestBody BookmarkRequestDto requestDto){

        if(requestDto.isBookmarkChecked()){
            return bookmarkService.saveBookmark(requestDto);
        }
        else {  return bookmarkService.deleteBookmark(requestDto);  }
    }
}
