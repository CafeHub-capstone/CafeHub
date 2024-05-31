package com.cafehub.cafehub.bookmark.service;

import com.cafehub.cafehub.bookmark.entity.Bookmark;
import com.cafehub.cafehub.bookmark.repository.BookmarkRepository;
import com.cafehub.cafehub.bookmark.request.BookmarkRequestDto;
import com.cafehub.cafehub.bookmark.response.BookmarkedCafeDetails;
import com.cafehub.cafehub.cafe.entity.Cafe;
import com.cafehub.cafehub.cafe.repository.CafeRepository;
import com.cafehub.cafehub.common.dto.ResponseDto;
import com.cafehub.cafehub.member.entity.Member;
import com.cafehub.cafehub.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookmarkServiceImpl implements BookmarkService{

    //궁금한게.. -> request 다 필요한거 아닌가..?
    private final BookmarkRepository bookmarkRepository;
    private final CafeRepository cafeRepository;
    private final MemberRepository memberRepository;
    //private final JwtProvider jwtProvider;

    //북마크 조회
    @Override
    public ResponseDto<?> getBookmarkList(HttpServletRequest request){
        /**
         * 추후 로그인 여부 확인
         */
        //Member member = getMemberFromJwt(request);
        Member member = memberRepository.findById(2l).get();

        List<BookmarkedCafeDetails> list = new ArrayList<>();
        List<Cafe> cafes= bookmarkRepository.findAllByMember_Id(member.getId());


        cafes.forEach(cafe -> list.add(new BookmarkedCafeDetails().builder()
                .cafeId(cafe.getId())
                .cafeName(cafe.getName())
                .cafePhotoUrl(cafe.getCafePhotoUrl())
                .cafeRating(cafe.getRating())
                .cafeTheme(cafe.getTheme().toString())
                .cafeReviewNum(cafe.getReviewCount())
                .build()));

        return ResponseDto.success(list);
    }

    //북마크 저장
    @Transactional
    @Override
    public ResponseDto<?> saveBookmark(BookmarkRequestDto bookmarkRequestDto, HttpServletRequest request) {

        Cafe cafe = cafeRepository.findById(bookmarkRequestDto.getCafeId()).get();
        //Member member = getMemberFromJwt(request);
        Member member = memberRepository.getOne(2l);

        // if(bookmarkRepository.existsByMemberIdAndCafeId(1L, request.getCafeId())) throw new MemberBookmarkAlreadyExistException();

        Bookmark bookmark = Bookmark.builder()
                .member(member)
                .cafe(cafe)
                .build();

        bookmarkRepository.save(bookmark);

        Long saveCafeId = bookmark.getCafe().getId();

        return ResponseDto.success(saveCafeId);
    }

    //북마크 삭제
    @Transactional
    @Override
    public ResponseDto<?> deleteBookmark(BookmarkRequestDto bookmarkRequestDto, HttpServletRequest request) {
        /**
         * 추후 로그인 여부 확인
         */
        //Member member = getMemberFromJwt(request);

        Member member = memberRepository.getOne(2l);
        Cafe cafe = cafeRepository.findById(bookmarkRequestDto.getCafeId()).orElse(null);

        //해당하는 cafe가 없는 경우-> 에러내용은 내가 작성?
        if (cafe == null) {
            return ResponseDto.success(0);//fail(ErrorCode.에러내용);
        }
        //원하는 카페 아이디가 실제 카페 아이디와 다른 경우->?이게 모지, 질문
        else if (!bookmarkRequestDto.getCafeId().equals(cafe.getId())) {
            return ResponseDto.success(0);//return ResponseDto.fail(ErrorCode.에러내용);
        }
        //일치하는 카페 아이디를 찾은 경우
        else {
            // 상상 이상으로 spring data jpa 가 제공하는 기능이 많음
            bookmarkRepository.deleteByCafeIdAndMemberId(bookmarkRequestDto.getCafeId(), member.getId());
            return ResponseDto.success(bookmarkRequestDto.getCafeId());
        }
    }

   /* private Member getMemberFromJwt(HttpServletRequest request) {
        Authentication authentication = jwtProvider.getAuthentication(request.getHeader("Authorization").substring(7));
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getMember();
    }*/

}
