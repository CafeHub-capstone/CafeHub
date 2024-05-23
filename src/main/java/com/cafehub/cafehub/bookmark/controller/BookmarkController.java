package com.cafehub.cafehub.bookmark.controller;

import com.cafehub.cafehub.bookmark.request.BookmarkRequestDto;
import com.cafehub.cafehub.bookmark.response.BookmarkListResponseDto;
import com.cafehub.cafehub.bookmark.response.BookmarkResponseDto;
import com.cafehub.cafehub.bookmark.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @GetMapping("/bookmark")
    public BookmarkListResponseDto bookmarkList(){
       return bookmarkService.getBookmarkList();
    }

    @PostMapping("/bookmark")
    public BookmarkResponseDto bookmarkSaveOrDelete(@RequestBody BookmarkRequestDto requestDto){

        if(requestDto.isBookmarkChecked()){
            return bookmarkService.saveBookmark(requestDto);
        }
        else {  return bookmarkService.deleteBookmark(requestDto);  }
    }
}
