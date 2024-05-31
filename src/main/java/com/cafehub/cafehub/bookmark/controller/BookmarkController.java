package com.cafehub.cafehub.bookmark.controller;

import com.cafehub.cafehub.bookmark.request.BookmarkRequestDto;
import com.cafehub.cafehub.bookmark.service.BookmarkService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    //북마크 조회 시, 사용자를 알기 위해 HttpServletRequest 추가
    @GetMapping("/bookmarks")
    public ResponseEntity<?> bookmarkList(HttpServletRequest request){
        return ResponseEntity.ok().body(bookmarkService.getBookmarkList(request));
    }

    @PostMapping("/bookmark")
    public ResponseEntity<?> bookmarkSaveOrDelete(@RequestBody BookmarkRequestDto requestDto, HttpServletRequest request){

        if(requestDto.isBookmarkChecked()){
            return ResponseEntity.ok().body(bookmarkService.saveBookmark(requestDto, request));
        }
        else {
            return ResponseEntity.ok().body(bookmarkService.deleteBookmark(requestDto, request));
        }
    }
}
