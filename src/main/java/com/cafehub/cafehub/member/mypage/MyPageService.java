package com.cafehub.cafehub.member.mypage;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.cafehub.cafehub.comment.entity.Comment;
import com.cafehub.cafehub.common.ErrorCode;
import com.cafehub.cafehub.common.dto.ResponseDto;
import com.cafehub.cafehub.likeReview.repository.LikeReviewRepository;
import com.cafehub.cafehub.member.entity.Member;
import com.cafehub.cafehub.member.mypage.dto.ProfileCommentsResponseDto;
import com.cafehub.cafehub.member.mypage.dto.ProfileRequestDto;
import com.cafehub.cafehub.member.mypage.dto.ProfileResponseDto;
import com.cafehub.cafehub.member.mypage.dto.ProfileReviewsResponseDto;
import com.cafehub.cafehub.member.mypage.exception.FailedChangeProfile;
import com.cafehub.cafehub.member.repository.MemberRepository;
import com.cafehub.cafehub.review.entity.Review;
import com.cafehub.cafehub.reviewPhoto.entity.ReviewPhoto;
import com.cafehub.cafehub.security.UserDetailsImpl;
import com.cafehub.cafehub.security.jwt.JwtProvider;
import com.cafehub.cafehub.security.jwt.RefreshTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MyPageService {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3Client s3Client;
    private final JwtProvider jwtProvider;
    private final ReviewRepository reviewRepository;
    private final ReviewPhotoRepository reviewPhotoRepository;
    private final LikeReviewRepository likeReviewRepository;
    private final CommentsRepository commentsRepository;

    public ResponseDto<?> getMyProfile(HttpServletRequest request) {
        Member member = getMemberFromJwt(request);
        ProfileResponseDto profileResponseDto = ProfileResponseDto.builder()
                .nickname(member.getNickname())
                .email(member.getEmail())
                .profileImg(member.getUserPhotoUrl())
                .build();

        return ResponseDto.success(profileResponseDto);
    }

    public ResponseDto<?> getMyReviews(Pageable pageable, HttpServletRequest request) {
        Member member = getMemberFromJwt(request);
        Page<Review> allMyReviews = reviewRepository.findAllByMemberId(member.getId(), pageable);
        List<Review> reviews = allMyReviews.getContent();
        List<ReviewResponseDto> myReviewList = makeReviewResponse(reviews);

        ProfileReviewsResponseDto profileReviewsResponseDto = ProfileReviewsResponseDto.builder()
                .reviewList(myReviewList)
                .isLast(allMyReviews.isLast())
                .currentPage(allMyReviews.getNumber())
                .build();

        return ResponseDto.success(profileReviewsResponseDto);
    }

    public ResponseDto<?> getMyComments(Pageable pageable, HttpServletRequest request) {
        Member member = getMemberFromJwt(request);
        Page<Comment> allMyComments = commentsRepository.findAllByMemberId(member.getId(), pageable);
        List<Comment> comments = allMyComments.getContent();
        List<CommentResponseDto> myCommentList = makeCommentResponse(comments);

        /**
         * 해당 부분은 댓글 도메인과 병합시 변경 될 예정
         */
        ProfileCommentsResponseDto profileCommentsResponseDto = ProfileCommentsResponseDto.builder()
                .commentList(myCommentList)
                .isLast(allMyComments.isLast())
                .currentPage(allMyComments.getNumber())
                .build();

        return ResponseDto.success(profileCommentsResponseDto);
    }

    public ResponseDto<?> changeMyProfile(HttpServletRequest request, ProfileRequestDto requestDto) {
        Member member = getMemberFromJwt(request);
        String nickname = requestDto.getNickname();
        Object profileImg = requestDto.getProfileImg();
        try {
            if (nickname != null) {
                member.updateNickname(nickname);
            }
            if (profileImg != null) {
                String userPhotoUrl = uploadS3((MultipartFile) profileImg, member);
                member.updateProfileImg(userPhotoUrl);
            }
            return ResponseDto.success("Profile Changed");
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new FailedChangeProfile(ErrorCode.FAILED_CHANGE_PROFILE);
        }
    }

    private List<ReviewResponseDto> makeReviewResponse(List<Review> reviews) {
        List<ReviewResponseDto> responseDtoList = new ArrayList<>();

        /**
         * 리뷰 ID 목록 추출
         */
        List<Long> reviewIds = reviews.stream().map(Review::getId).toList();

        /**
         * 리뷰 ID 목록을 통해서 ReviewPhoto 목록 추출
         */
        List<ReviewPhoto> reviewPhotos = reviewPhotoRepository.findAllByReviewIdIn(reviewIds);

        /**
         * KEY = 리뷰 ID, VALUE = ReviewPhoto 맵 생성
         */
        Map<Long, List<ReviewPhoto>> reviewPhotosMap = reviewPhotos.stream()
                .collect(Collectors.groupingBy(reviewPhoto -> reviewPhoto.getReview().getId()));

        /**
         * 리뷰 리스트 생성 및 반환
         */
        for (Review review : reviews) {
            ReviewResponseDto responseDto = ReviewResponseDto.builder()
                    .reviewId(review.getId())
                    .author(review.getMember().getNickname())
                    .reviewRating(review.getRating())
                    .reviewContent(review.getContent())
                    .reviewCreateDate(review.getCreatedDate())
                    .likeCnt(review.getLikeCount())
                    .likeChecked(checkLike(review.getMember(), review))
                    .commentCnt(review.getCommentCount())
                    .photoUrls(reviewPhotosMap.getOrDefault(review.getId(), Collections.emptyList()).stream()
                            .map(reviewPhoto -> new PhotoUrlResponse(reviewPhoto.getReviewPhotoUrl()))
                            .collect(Collectors.toList()))
                    .reviewManagement(true)
                    .build();
            responseDtoList.add(responseDto);
        }
        return responseDtoList;
    }

    private Boolean checkLike(Member member, Review review) {
        return likeReviewRepository.existsByMemberAndReview(member, review);
    }

    private List<CommentResponseDto> makeCommentResponse(List<Comment> comments) {
        List<CommentResponseDto> responseDtoList = new ArrayList<>();

        for (Comment comment : comments) {
            CommentResponseDto responseDto = CommentResponseDto.builder()
                    .commentId(comment.getId())
                    .author(comment.getMember().getNickname())
                    .commentContent(comment.getContent())
                    .commentCreateDate(comment.getCreatedDate())
                    .commentManagement(true)
                    .build();
            responseDtoList.add(responseDto);
        }
        return responseDtoList;
    }

//    public ResponseDto<?> deleteMember(HttpServletRequest request) {
//        Long memberId = getMemberFromJwt(request).getId();
//        String email = getMemberFromJwt(request).getEmail();
//    }

    private Member getMemberFromJwt(HttpServletRequest request) {
        Authentication authentication = jwtProvider.getAuthentication(request.getHeader("Authorization").substring(7));
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getMember();
    }

    private String uploadS3(MultipartFile profileImg, Member member) throws IOException {
        String userPhotoUrl = member.getUserPhotoUrl();
        String basicImg = "아마존S3주소";
        if (userPhotoUrl!=null && !userPhotoUrl.equals(basicImg)) {
            fileDelete(userPhotoUrl);
        }
        String s3FileName = UUID.randomUUID() + "-" + profileImg.getOriginalFilename();
        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentLength(profileImg.getSize());
        objMeta.setContentType(profileImg.getContentType());
        s3Client.putObject(bucket, s3FileName, profileImg.getInputStream(), objMeta);
        return s3Client.getUrl(bucket, s3FileName).toString();

    }

    private void fileDelete(String userPhotoUrl) {
        try {
            String decodeVal = URLDecoder.decode(userPhotoUrl.substring(51), StandardCharsets.UTF_8);
            s3Client.deleteObject(this.bucket,decodeVal);
        } catch (AmazonServiceException e) {
            log.error(e.getErrorMessage());
        }
    }
}
