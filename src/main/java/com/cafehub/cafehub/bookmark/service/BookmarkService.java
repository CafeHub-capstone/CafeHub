package com.cafehub.cafehub.bookmark.service;

import com.cafehub.cafehub.bookmark.request.BookmarkRequestDto;
import com.cafehub.cafehub.common.dto.ResponseDto;
import jakarta.servlet.http.HttpServletRequest;

public interface BookmarkService {
    //리스트 조회
    ResponseDto<?> getBookmarkList(HttpServletRequest request);
    //등록
    ResponseDto<?> saveBookmark (BookmarkRequestDto bookmarkRequestDto, HttpServletRequest request);
    //삭제
    ResponseDto<?> deleteBookmark(BookmarkRequestDto bookmarkRequestDto, HttpServletRequest request);
}
