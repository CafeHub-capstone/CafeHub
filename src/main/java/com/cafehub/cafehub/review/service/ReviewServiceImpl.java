package com.cafehub.cafehub.review.service;

import com.cafehub.cafehub.cafe.entity.Cafe;
import com.cafehub.cafehub.cafe.repository.CafeRepository;
import com.cafehub.cafehub.comment.entity.Comment;
import com.cafehub.cafehub.comment.repository.CommentRepository;
import com.cafehub.cafehub.likeReview.repository.LikeReviewRepository;
import com.cafehub.cafehub.member.entity.Member;
import com.cafehub.cafehub.member.repository.MemberRepository;
import com.cafehub.cafehub.review.entity.Review;
import com.cafehub.cafehub.review.repository.ReviewRepository;
import com.cafehub.cafehub.review.request.*;
import com.cafehub.cafehub.review.response.*;
import com.cafehub.cafehub.reviewPhoto.entity.ReviewPhoto;
import com.cafehub.cafehub.reviewPhoto.repository.ReviewPhotoRepository;
import com.cafehub.cafehub.reviewPhoto.response.PhotoUrlResponse;
import com.cafehub.cafehub.s3.S3Manager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j // 이것도 테스트용.
@RequiredArgsConstructor
@Transactional(readOnly = true) // 필요한 것만 false.
// db에서 작동하는 데이터 단위. 여기선 필요 없음.
public class ReviewServiceImpl implements ReviewService {

    private static final int REVIEW_PAGING_SIZE = 10;

    private final CafeRepository cafeRepository;

    private final MemberRepository memberRepository;

    private final ReviewRepository reviewRepository;

    private final ReviewPhotoRepository reviewPhotoRepository;

    private final LikeReviewRepository likeReviewRepository;

    private final CommentRepository commentRepository;

    private final S3Manager s3Manager;

    @Override
    public AllReviewGetResponse getAllReviewOfCafe(AllReviewGetRequest request) {

        // 현재 로그인한 사용자의 이메일을 가져옴
        String currentMemberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println(currentMemberEmail);
        // 현재 로그인한 사용자의 아이디를 가져옴
        Member loginMember =  memberRepository.findByEmail(currentMemberEmail).orElse(null);
        Long currentMemberId =null;
        if(loginMember!=null) {
            currentMemberId = loginMember.getId();
        }

        Slice<Review> reviews = reviewRepository.findAllByCafeId(
                PageRequest.of(request.getCurrentPage(), REVIEW_PAGING_SIZE, Sort.by(Sort.Direction.DESC, "createdAt")),
                request.getCafeId());

        // 리뷰 ID 목록을 추출
        List<Long> reviewIds = reviews.stream().map(Review::getId).collect(Collectors.toList());

        // 리뷰 ID 목록을 기반으로 모든 ReviewPhoto를 한 번에 가져옴.
        List<ReviewPhoto> reviewPhotos = reviewPhotoRepository.findAllByReviewIdIn(reviewIds);

        // 리뷰 ID를 키로 하는 ReviewPhoto 리스트 맵을 생성.
        Map<Long, List<ReviewPhoto>> reviewPhotosMap = reviewPhotos.stream()
                .collect(Collectors.groupingBy(reviewPhoto -> reviewPhoto.getReview().getId()));


        Set<Long> likedReviewIdSet;
        if (loginMember!=null) {
            // 현재 멤버가 좋아요한 리뷰 ID 리스트 생성.
            List<Long> currentMemberLikeReviewIds = likeReviewRepository.findByMemberIdAndReviewIdIn(currentMemberId, reviewIds)
                    .stream()
                    .map(likeReview -> likeReview.getReview().getId())
                    .toList();

            // 좋아요한 리뷰 ID를 Set으로 변환
            likedReviewIdSet = new HashSet<>(currentMemberLikeReviewIds);
        } else {
            likedReviewIdSet = Collections.emptySet();
        }
        // 날짜 포맷터 설정
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // 리뷰 리스트 생성.
        List<ReviewResponse> reviewList = reviews.stream().map(review -> {
            List<PhotoUrlResponse> photoUrls = reviewPhotosMap.getOrDefault(review.getId(), Collections.emptyList()).stream()
                    .map(reviewPhoto -> new PhotoUrlResponse(reviewPhoto.getReviewPhotoUrl()))
                    .collect(Collectors.toList());
            Boolean isliked = null;
            if (loginMember != null) {
                // 사용자가 이 리뷰를 좋아요 했는지 확인
                isliked = likedReviewIdSet.contains(review.getId());
//            Boolean isliked = currentMemberLikeReviewIds.contains(review.getId());
            }
            // 사용자가 이 리뷰를 작성했는지 확인
            boolean isReviewOwner = review.getMember().getEmail().equals(currentMemberEmail);

            return ReviewResponse.builder()
                    .reviewId(review.getId())
                    .author(review.getMember().getNickname())
                    .reviewRating(review.getRating())
                    .reviewContent(review.getContent())
                    .reviewCreateAt(review.getCreatedAt()) // 날짜를 yyyy-MM-dd 형식으로 포맷
                    .likeCnt(review.getLikeCount())
                    .likeChecked(isliked)
                    .commentCnt(review.getCommentCount())
                    .photoUrls(photoUrls)
                    .reviewManagement(isReviewOwner) // 로그인 미구현으로 리뷰 관리 false
                    .build();
        }).collect(Collectors.toList());

        Cafe cafe = cafeRepository.findById(request.getCafeId()).get();

        return new AllReviewGetResponse(true, reviewList, getCafeRatingAVG(cafe), cafe.getReviewCount(), reviews.isLast(), reviews.getNumber(), "ok");
    }

    @Override
    @Transactional
    public ReviewCreateResponse writeReview(ReviewCreateRequest request, List<MultipartFile> photos){
        // 이미 리뷰를 작성한 사람은 리뷰 작성 못하게 기능 추가해야함.

        // 로그인 된 사람만 통과 시키는 로직이 필요함
        String currentMemberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Member loginMember = memberRepository.findByEmail(currentMemberEmail).orElse(null);

        // 카페와 회원 정보를 가져옴
        Cafe cafe = cafeRepository.findById(request.getCafeId()).get();

        Review review = Review.builder()
                .rating(request.getReviewRating())
                .content(request.getReviewContent())
                .likeCount(0)
                .commentCount(0)
                .cafe(cafeRepository.findById(request.getCafeId()).get())
                .member(loginMember)
                .build();
        reviewRepository.save(review);

        List<ReviewPhoto> reviewPhotos = new ArrayList<>();

        if (photos!=null) {
            for (MultipartFile photoRequest : photos) {

                String reviewPhotoKey = s3Manager.generateReviewPhotoKeyName();
                String reviewPhothourl = s3Manager.uploadFile(reviewPhotoKey, photoRequest);


                ReviewPhoto reviewPhoto = ReviewPhoto.builder()
                        .reviewPhotoUrl(reviewPhothourl)
                        .reviewPhotoKey(reviewPhotoKey)
                        .review(review)
                        .build();
                reviewPhotos.add(reviewPhoto); // 매번 DB에 담지 않고, 우선 List에 담기.
            }
            // 한 번에 DB에 저장
            reviewPhotoRepository.saveAll(reviewPhotos);
        }
        cafe.updateRating(getCafeRatingAVG(cafe));
        cafe.updateReviewCount(cafe.getReviews().size());

        return new ReviewCreateResponse(true, review.getId(),"ok");
    }

    // 카페 평점 평균 구하는 메서드.
    private BigDecimal getCafeRatingAVG(Cafe cafe){

        List<Review> reviews = cafe.getReviews();
        if(reviews.isEmpty()) return BigDecimal.valueOf(0);

        Double sum = (double) 0;
        for(Review review: reviews)
            sum += review.getRating();

        return BigDecimal.valueOf(sum/reviews.size()).setScale(1, RoundingMode.HALF_UP); // 리뷰 평점 평균.
    }

    @Override
    @Transactional
    public ReviewUpdateResponse updateReview(ReviewUpdateRequest request, List<MultipartFile> photos){

        Review prevReview = reviewRepository.findById(request.getReviewId()).get();

        // 리뷰 업데이트
        prevReview.updateContent(request.getReviewRating(), request.getReviewContent());

        // 리뷰 저장은 이미 영속성 컨텍스트에 포함되어 있으므로 별도의 save 호출 불필요

        // 카페 업데이트를 위해 카페 ID 가져오기
        Long cafeId = prevReview.getCafe().getId();
        Cafe prevCafe = cafeRepository.findById(cafeId).get();

        prevCafe.updateRating(getCafeRatingAVG(prevCafe));
        prevCafe.updateReviewCount(prevCafe.getReviews().size());
        // 카페 저장은 영속성 컨텍스트에 포함되어 있으므로 별도의 save 호출 불필요

        // S3는 DB IN 처럼 한번에 업로드 하는 방법이 없는거 같음
        List<ReviewPhoto> reviewPhotoList = reviewPhotoRepository.findAllByReviewId(request.getReviewId());

        if (!reviewPhotoList.isEmpty()) {
            for (ReviewPhoto reviewPhoto : reviewPhotoList) {
                s3Manager.deleteFile(reviewPhoto.getReviewPhotoKey());
            }
            reviewPhotoRepository.deleteAllByReviewId(request.getReviewId());
        }


        // 새로운 리뷰 사진들을 리스트에 담음


        List<ReviewPhoto> reviewPhotos = new ArrayList<>();
        if (photos != null) {
            for (MultipartFile photoRequest : photos) {

                String reviewPhotoKey = s3Manager.generateReviewPhotoKeyName();
                String reviewPhothourl = s3Manager.uploadFile(reviewPhotoKey, photoRequest);

                ReviewPhoto reviewPhoto = ReviewPhoto.builder()
                        .reviewPhotoUrl(reviewPhothourl)
                        .reviewPhotoKey(reviewPhotoKey)
                        .review(prevReview) // 기존 리뷰 객체를 사용
                        .build();
                reviewPhotos.add(reviewPhoto);
            }
            // 리뷰 사진들을 한 번에 저장
            reviewPhotoRepository.saveAll(reviewPhotos);
        }
        return new ReviewUpdateResponse(true, prevReview.getId(),"ok");
    }

    @Override
    @Transactional
    public ReviewDeleteResponse deleteReview(ReviewDeleteRequest request){
        // 이게 내 리뷰가 맞는지 확인하는 작업 필요.
        Review review = reviewRepository.findById(request.getReviewId()).get();

        // 리뷰 사진 삭제 - cascade가 아니므로 직접 해줘야 함.
        List<ReviewPhoto> reviewPhotoList = reviewPhotoRepository.findAllByReviewId(request.getReviewId());

        if(reviewPhotoList!=null) {
            for (ReviewPhoto reviewPhoto : reviewPhotoList) {
                s3Manager.deleteFile(reviewPhoto.getReviewPhotoKey());
            }
            reviewPhotoRepository.deleteAllByReviewId(request.getReviewId());
        }

        // 리뷰의 댓글 삭제

        List<Comment> reviewCommentList = commentRepository.findAllByReviewId(request.getReviewId());


        if (reviewCommentList != null) {
            commentRepository.deleteAllByReviewId(request.getReviewId());
        }


        // 리뷰 삭제.
        reviewRepository.delete(review);

        // 카페 정보 업데이트
        Cafe cafe = review.getCafe();

        // 리뷰 삭제 후 카페의 리뷰 수와 평점 업데이트
        cafe.updateRating(getCafeRatingAVG(cafe));
        cafe.updateReviewCount(cafe.getReviews().size());

        // 카페 저장은 이미 영속성 컨텍스트에 포함되어 있으므로 별도의 save 호출 불필요

        return new ReviewDeleteResponse(true, request.getReviewId(), "ok");
    }
}