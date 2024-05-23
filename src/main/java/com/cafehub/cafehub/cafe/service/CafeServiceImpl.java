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

    // 카페 전체 리스트를 불러오는 로직 : DB와 통신하는 횟수 : 1번
    @Override
    public CafeListResponse getCafeListResponseSortedByType(CafeListRequest cafeListRequest) {

        Slice<Cafe> cafes;

        // 현재 카페의 테마에는 Study, Date, Meet, Dessert 가 있고 모든 테마를 조회 하고 싶을 때는 All
        if (cafeListRequest.getTheme().equals("All")) cafes = cafeRepository.findAllFetch(cafeListRequest);
        else cafes = cafeRepository.findAllByThemeFetch(cafeListRequest);

        List<Cafe> cafeList = cafes.getContent();

        // (체크 포인트) 여기서 계속 new DTO가 생성되는데 괜찮을까에 대한 고민을 한번 해봐야 할듯
        List<CafeResponseForCafeList> cafeListResponse = cafeList.stream()
                .map(cafe -> new CafeResponseForCafeList(
                        cafe.getId(),
                        cafe.getCafePhotoUrl(),
                        cafe.getName(),
                        cafe.getTheme().getName(),
                        cafe.getRating(),
                        cafe.getReviewCount()))
                .toList(); // 자바 16 이상부터 .collect(Collectors.toList()) => toList() 가능

        return new CafeListResponse(true, cafeListResponse,cafes.hasNext(),cafes.getNumber(), "ok");
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
