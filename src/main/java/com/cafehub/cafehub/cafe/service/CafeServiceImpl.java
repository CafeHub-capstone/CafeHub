package com.cafehub.cafehub.cafe.service;


import com.cafehub.cafehub.bookmark.repository.BookmarkRepository;
import com.cafehub.cafehub.cafe.entity.Cafe;
import com.cafehub.cafehub.cafe.repository.CafeRepository;
import com.cafehub.cafehub.cafe.request.CafeInfoRequest;
import com.cafehub.cafehub.cafe.request.CafeListRequest;
import com.cafehub.cafehub.cafe.response.CafeInfoResponse;
import com.cafehub.cafehub.cafe.response.CafeListResponse;
import com.cafehub.cafehub.cafe.response.CafeResponseForCafeList;
import com.cafehub.cafehub.likeReview.entity.LikeReview;
import com.cafehub.cafehub.likeReview.repository.LikeReviewRepository;
import com.cafehub.cafehub.member.entity.Member;
import com.cafehub.cafehub.member.repository.MemberRepository;
import com.cafehub.cafehub.menu.entity.Menu;
import com.cafehub.cafehub.menu.repository.MenuRepository;
import com.cafehub.cafehub.menu.response.BestMenuResponse;
import com.cafehub.cafehub.menu.response.MenuResponse;
import com.cafehub.cafehub.review.entity.Review;
import com.cafehub.cafehub.review.repository.ReviewRepository;
import com.cafehub.cafehub.review.response.BestReviewResponse;
import com.cafehub.cafehub.reviewPhoto.entity.ReviewPhoto;
import com.cafehub.cafehub.reviewPhoto.response.PhotoUrlResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CafeServiceImpl implements CafeService{

    private final CafeRepository cafeRepository;

    private final MenuRepository menuRepository;

    private final ReviewRepository reviewRepository;

    private final BookmarkRepository bookmarkRepository;

    private final LikeReviewRepository likeReviewRepository;

    private final MemberRepository memberRepository;


    @Override
    public CafeListResponse getCafeListResponseSortedByType(CafeListRequest cafeListRequest) {

        Slice<Cafe> cafes;
        if (cafeListRequest.getTheme().equals("All")) cafes = cafeRepository.findAllFetch(cafeListRequest);
        else cafes = cafeRepository.findByThemeFetchSortedByType(cafeListRequest);

        List<Cafe> cafeList = cafes.getContent();
        List<CafeResponseForCafeList> cafeResponseForCafeListList = new ArrayList<>();

        for (Cafe cafe : cafeList){
            CafeResponseForCafeList cafeResponseForCafeList = new CafeResponseForCafeList();
            setCafeResponse(cafe, cafeResponseForCafeList);
            cafeResponseForCafeListList.add(cafeResponseForCafeList);
        }

        return new CafeListResponse(true, cafeResponseForCafeListList,cafes.hasNext(),cafeListRequest.getCurrentPage(), "Theme : " + cafeListRequest.getTheme() + ", SortedByType : " + cafeListRequest.getSortedByType() + " 으로 카페리스트 반환 성공");
    }

    private void setCafeResponse(Cafe cafe, CafeResponseForCafeList cafeResponseForCafeList){

        cafeResponseForCafeList.setCafeId(cafe.getId());
        cafeResponseForCafeList.setCafePhotoUrl(cafe.getCafePhotoUrl());
        cafeResponseForCafeList.setCafeName(cafe.getName());
        cafeResponseForCafeList.setCafeRating(cafe.getRating());
        cafeResponseForCafeList.setCafeTheme(cafe.getTheme().getName());
        cafeResponseForCafeList.setCafeReviewNum(cafe.getReviewCount());
    }


    @Override
    public CafeInfoResponse getCafeInfo(CafeInfoRequest cafeInfoRequest){

        // optional 처리 나중에 예외처리 할때 같이할 예정
        Cafe cafe = cafeRepository.findById(cafeInfoRequest.getCafeId()).get();

        // 이 부분은 수정이 있을거임 주석 부분 코드를 쓸거고 현재 코드는 임시

        // String currentMemberEmail = SecurityContextHolder.getContext().getAuthentication().getEmail();
        String currentMemberEmail = SecurityContextHolder.getContext().getAuthentication().getName();

//        Member loginMember = memberRepository.findByEmail(currentMemberEmail);
        Member loginMember = memberRepository.findByUsername(currentMemberEmail);


        boolean bookmarkChecked = false;

        if (loginMember != null) {
            // Boolean bookmarkChecked = bookmarkRepository.existsByCafeIdAndMemberEmail(cafeInfoRequest.getCafeId(), currentMemberEmail);
            bookmarkChecked = bookmarkRepository.existsByCafeIdAndMemberId(cafeInfoRequest.getCafeId(), loginMember.getId());
        }

        List<Menu> bestMenus = menuRepository.findBestMenuFetch(cafeInfoRequest.getCafeId());

        List<BestMenuResponse> bestMenuList = bestMenus.stream()
                .map(menu -> new BestMenuResponse(
                        menu.getId(),
                        menu.getName(),
                        menu.getPrice()))
                .toList();


        // 현재는 best 리뷰가 2개밖에 없어서 이건 너무 과한 로직 일 수 있는데 , 추후 best 리뷰를 5개 보여주기 이런식으로 변경이 될 것을 고려
        List<Review> bestReview = reviewRepository.findBestReviewFetch(cafe.getId());
        List<BestReviewResponse> bestReviewResponseList;

        Set<Long> likedReviewIds; // 기본값으로 빈 Set을 설정

        if (loginMember != null) {

            // 리뷰 ID 목록 추출
            List<Long> reviewIds = bestReview.stream()
                    .map(Review::getId)
                    .toList();

            // "좋아요" 상태를 한 번의 쿼리로 가져오기
            List<LikeReview> likeReviews = likeReviewRepository.findByMemberIdAndReviewIdIn(loginMember.getId(), reviewIds);

            // Set으로 변환하여 저장, 나중에 탐색할때 List 면 전수 순회 O(N) , 셋이나 맵이면 O(1)
            likedReviewIds = likeReviews.stream()
                    .map(likeReview -> likeReview.getReview().getId())
                    .collect(Collectors.toSet());
        } else {
            likedReviewIds = Collections.emptySet();
        }


        bestReviewResponseList = bestReview.stream()
                .map(review -> {
                    BestReviewResponse bestReviewResponse = new BestReviewResponse(
                            review.getId(),
                            review.getMember().getNickname(),
                            review.getRating(),
                            review.getContent(),
                            review.getCreatedDate(),
                            review.getLikeReviewCount(),
                            review.getCommentCount(),
                            likedReviewIds.contains(review.getId()),  // 로그인 한 사용자의 해당 카페의 좋아요 리스트를 셋으로 만들어뒀으니 거기서 탐색, 로그인 안했으면 null
                            null
                    );

                    // photoUrls 세팅, review Entity의 photoUrl OneToMany 사용
                    List<ReviewPhoto> photos = review.getReviewPhotos();
                    List<PhotoUrlResponse> photoUrlResponseList = photos.stream()
                            .map(photo -> new PhotoUrlResponse(photo.getReviewPhotoUrl()))
                            .toList();

                    bestReviewResponse.setPhotoUrls(photoUrlResponseList);
                    return bestReviewResponse;
                })
                .toList();



        return new CafeInfoResponse(
                true,
                cafe.getId(),
                cafe.getCafePhotoUrl(),
                cafe.getName(),
                cafe.getTheme().getName(),
                cafe.getReviewCount(),
                cafe.getOperationHours(),
                cafe.getAddress(),
                cafe.getPhone(),
                cafe.getRating(),
                bookmarkChecked,
                bestMenuList,
                bestReviewResponseList,
                "ok"
        );
    }

}
