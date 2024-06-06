package com.cafehub.cafehub.cafe.service;

import com.cafehub.cafehub.bookmark.repository.BookmarkRepository;
import com.cafehub.cafehub.cafe.entity.Cafe;
import com.cafehub.cafehub.cafe.exception.CafeNotFoundException;
import com.cafehub.cafehub.cafe.repository.CafeRepository;
import com.cafehub.cafehub.cafe.request.CafeInfoRequestDTO;
import com.cafehub.cafehub.cafe.request.CafeListGetRequestDTO;
import com.cafehub.cafehub.cafe.response.CafeDetailsDTO;
import com.cafehub.cafehub.cafe.response.CafeInfoResponseDTO;
import com.cafehub.cafehub.cafe.response.CafeListResponseDTO;
import com.cafehub.cafehub.common.dto.ResponseDto;
import com.cafehub.cafehub.likeReview.entity.LikeReview;
import com.cafehub.cafehub.likeReview.repository.LikeReviewRepository;
import com.cafehub.cafehub.member.entity.Member;
import com.cafehub.cafehub.member.repository.MemberRepository;
import com.cafehub.cafehub.menu.entity.Menu;
import com.cafehub.cafehub.menu.repository.MenuRepository;
import com.cafehub.cafehub.menu.response.BestMenuResponseDTO;
import com.cafehub.cafehub.review.entity.Review;
import com.cafehub.cafehub.review.repository.ReviewRepository;
import com.cafehub.cafehub.review.response.BestReviewResponse;
import com.cafehub.cafehub.reviewPhoto.entity.ReviewPhoto;
import com.cafehub.cafehub.reviewPhoto.response.PhotoUrlResponse;
import com.cafehub.cafehub.security.UserDetailsImpl;
import com.cafehub.cafehub.security.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CafeServiceImpl implements CafeService{

    private static final int BEST_REVIEW_SIZE = 2;

    private final CafeRepository cafeRepository;

    private final MenuRepository menuRepository;

    private final ReviewRepository reviewRepository;

    private final BookmarkRepository bookmarkRepository;

    private final LikeReviewRepository likeReviewRepository;

    private final MemberRepository memberRepository;

    private final JwtProvider jwtProvider;


    // DB connection : 1
    @Override
    public ResponseDto<?> getCafeListSortedByType(CafeListGetRequestDTO request) {

        Slice<Cafe> cafes = request.getTheme().equals("All") ? cafeRepository.findAllFetch(request) : cafeRepository.findAllByThemeFetch(request);

        List<CafeDetailsDTO> cafeListResponse = cafes.getContent().stream()
                .map(this::buildCafeDetailsDTO)
                .toList();


        CafeListResponseDTO responseDTO = new CafeListResponseDTO(cafeListResponse,cafes.hasNext(), cafes.getNumber());

        return ResponseDto.success(responseDTO);
    }

    private CafeDetailsDTO buildCafeDetailsDTO(Cafe cafe) {

        CafeDetailsDTO cafeDetailsDTO = CafeDetailsDTO.builder()
                .cafeId(cafe.getId())
                .cafePhotoUrl(cafe.getCafePhotoUrl())
                .cafeName(cafe.getName())
                .cafeTheme(cafe.getTheme().getName())           // cafe left join theme (fetch join)
                .cafeRating(cafe.getRating())
                .cafeReviewNum(cafe.getReviewCount())
                .build();

        return cafeDetailsDTO;
    }


    // DB connection : cafeinfo(1) + bookmarkchecked(2) + bestMenu(1) + bestReview(2) + photourl(0) = 6번
    // 카페 정보 + 현재 로그인한 사용자가 해당카페에 북마크를 눌렀는지 + 베스트 메뉴 + 베스트 리뷰 + 현재 로그인한 사용자가 베스트 리뷰들에 좋아요를 눌렀는지
    @Override
    public ResponseDto<?> getCafeInfo(CafeInfoRequestDTO request){

        Cafe cafe = getCafeById(request.getCafeId());

        Member loginMember = getLoginMember();
        Boolean bookmarkChecked = isBookmarkChecked(cafe.getId(), loginMember);

        List<BestMenuResponseDTO> bestMenuList = getBestMenuList(cafe.getId());

        List<Review> topNReview = getTopNReviews(cafe.getId());
        Set<Long> likedReviewIds = getLikedReviewIds(topNReview, loginMember);

        List<BestReviewResponse> bestReviewList = getBestReviewList(topNReview, likedReviewIds);


        return ResponseDto.success(buildCafeInfoResponse(cafe, bookmarkChecked, bestMenuList, bestReviewList));
    }


    private Cafe getCafeById(Long cafeId) {
        return cafeRepository.findByIdWithTheme(cafeId).orElseThrow(CafeNotFoundException::new);
    }

    private Member getLoginMember() {
        String currentMemberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println(currentMemberEmail);
        return memberRepository.findByEmail(currentMemberEmail).orElse(null);
    }

    private Boolean isBookmarkChecked(Long cafeId, Member loginMember) {
        if (loginMember != null) {
            return bookmarkRepository.existsByCafeIdAndMemberId(cafeId, loginMember.getId());
        }
        return false;
    }

    private List<BestMenuResponseDTO> getBestMenuList(Long cafeId) {
        List<Menu> bestMenus = menuRepository.findAllByCafeIdAndBestTrue(cafeId);
        return bestMenus.stream()
                .map(menu -> new BestMenuResponseDTO(menu.getId(), menu.getName(), menu.getPrice()))
                .toList();
    }

    private List<Review> getTopNReviews(Long cafeId) {
        return reviewRepository.findAllByCafeIdWithMemberAndPhotos(cafeId,
                PageRequest.of(0, BEST_REVIEW_SIZE, Sort.by(Sort.Direction.DESC, "likeCount")));
    }

    private Set<Long> getLikedReviewIds(List<Review> topNReview, Member loginMember) {
        if (loginMember != null) {
            List<Long> reviewIds = topNReview.stream()
                    .map(Review::getId)
                    .toList();
            List<LikeReview> likeReviews = likeReviewRepository.findByMemberIdAndReviewIdIn(loginMember.getId(), reviewIds);
            return likeReviews.stream()
                    .map(likeReview -> likeReview.getReview().getId())
                    .collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }

    private List<BestReviewResponse> getBestReviewList(List<Review> topNReview, Set<Long> likedReviewIds) {
        return topNReview.stream()
                .map(review -> {
                    List<ReviewPhoto> photos = review.getReviewPhotos();
                    List<PhotoUrlResponse> photoUrlResponseList = photos.stream()
                            .map(photo -> new PhotoUrlResponse(photo.getReviewPhotoUrl()))
                            .toList();
                    return BestReviewResponse.builder()
                            .reviewId(review.getId())
                            .author(review.getMember().getNickname())
                            .reviewRating(review.getRating())
                            .reviewContent(review.getContent())
                            .createdAt(review.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                            .likeCnt(review.getLikeCount())
                            .commentCnt(review.getCommentCount())
                            .likeChecked(likedReviewIds.contains(review.getId()))
                            .photoUrls(photoUrlResponseList)
                            .build();
                })
                .toList();
    }

    private CafeInfoResponseDTO buildCafeInfoResponse(Cafe cafe, boolean bookmarkChecked,
                                                      List<BestMenuResponseDTO> bestMenuList,
                                                      List<BestReviewResponse> bestReviewList) {


        CafeInfoResponseDTO cafeInfoResponseDTO = CafeInfoResponseDTO.builder()
                .cafeId(cafe.getId())
                .cafePhotoUrl(cafe.getCafePhotoUrl())
                .cafeName(cafe.getName())
                .cafeTheme(cafe.getTheme().getName())               // cafe left join theme (fetch join)
                .cafeReviewCnt(cafe.getReviewCount())
                .cafeOperationHour(cafe.getOperationHours())
                .cafeAddress(cafe.getAddress())
                .cafePhone(cafe.getPhone())
                .cafeRating(cafe.getRating())
                .bookmarkChecked(bookmarkChecked)
                .bestMenuList(bestMenuList)
                .bestReviewList(bestReviewList)
                .build();

        return cafeInfoResponseDTO;
    }


    private Member getMemberFromJwt(HttpServletRequest request) {
        Authentication authentication = jwtProvider.getAuthentication(request.getHeader("Authorization").substring(7));
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getMember();
    }

}