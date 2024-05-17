package com.cafehub.cafehub.bookmark.service;

import com.cafehub.cafehub.bookmark.entity.Bookmark;
//import com.cafehub.cafehub.bookmark.exception.MemberBookmarkAlreadyExistException;
import com.cafehub.cafehub.bookmark.repository.BookmarkRepository;
import com.cafehub.cafehub.bookmark.request.BookmarkRequestDto;
import com.cafehub.cafehub.bookmark.response.BookmarkListResponseDto;
import com.cafehub.cafehub.bookmark.response.BookmarkResponseDto;
import com.cafehub.cafehub.cafe.entity.Cafe;
import com.cafehub.cafehub.cafe.repository.CafeRepository;
import com.cafehub.cafehub.member.entity.Member;
import com.cafehub.cafehub.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookmarkServiceImpl implements BookmarkService{

    private final BookmarkRepository bookmarkRepository;
    private final CafeRepository cafeRepository;
    private final MemberRepository memberRepository;

    //북마크 조회
    @Override
    public BookmarkListResponseDto getBookmarkList(){

        Member member = memberRepository.getOne(2l);
        return new BookmarkListResponseDto(true, bookmarkRepository.findCafesByBookmarkIdAndMemberId(member.getId()), "OK");
    }

    //북마크 저장
    @Transactional
    @Override
    public BookmarkResponseDto saveBookmark(BookmarkRequestDto request) {

        Cafe cafe = cafeRepository.getOne(request.getCafeId());
        Member member = memberRepository.getOne(2l);

       // if(bookmarkRepository.existsByMemberIdAndCafeId(1L, request.getCafeId())) throw new MemberBookmarkAlreadyExistException();

        Bookmark bookmark = Bookmark.builder().member(member).cafe(cafe).build();
        bookmarkRepository.save(bookmark);
        Long saveCafeId = bookmark.getCafe().getId();

        return new BookmarkResponseDto(true, saveCafeId, "OK");
    }

    //북마크 삭제
    @Transactional
    @Override
    public BookmarkResponseDto deleteBookmark(BookmarkRequestDto request) {

        Member member = memberRepository.getOne(2l);
        Long deleteCafeId = request.getCafeId();

        bookmarkRepository.deleteByBookmarkIdAndMemberId(request.getCafeId(), member.getId());
        return new BookmarkResponseDto(true, deleteCafeId,"deleteComplete");
    }

}
