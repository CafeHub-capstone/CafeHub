package com.cafehub.cafehub.bookmark.service;

import com.cafehub.cafehub.bookmark.request.BookmarkRequestDto;
import com.cafehub.cafehub.bookmark.response.BookmarkListResponseDto;
import com.cafehub.cafehub.bookmark.response.BookmarkResponseDto;

public interface BookmarkService {
    //리스트 조회
    BookmarkListResponseDto getBookmarkList();
    //등록
    BookmarkResponseDto saveBookmark (BookmarkRequestDto request);
    //삭제
    BookmarkResponseDto deleteBookmark(BookmarkRequestDto request);
}
