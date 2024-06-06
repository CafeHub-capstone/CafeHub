package com.cafehub.cafehub.member.mypage;

import com.cafehub.cafehub.comment.entity.Comment;
import com.cafehub.cafehub.comment.repository.CommentRepository;
import com.cafehub.cafehub.comment.response.CommentResponseDTO;
import com.cafehub.cafehub.common.dto.ResponseDto;
import com.cafehub.cafehub.likeReview.repository.LikeReviewRepository;
import com.cafehub.cafehub.member.entity.Member;
import com.cafehub.cafehub.member.mypage.dto.ProfileCommentsResponseDto;
import com.cafehub.cafehub.member.mypage.dto.ProfileRequestDto;
import com.cafehub.cafehub.member.mypage.dto.ProfileResponseDto;
import com.cafehub.cafehub.member.mypage.dto.ProfileReviewsResponseDto;
import com.cafehub.cafehub.review.entity.Review;
import com.cafehub.cafehub.review.repository.ReviewRepository;
import com.cafehub.cafehub.review.response.ReviewResponse;
import com.cafehub.cafehub.reviewPhoto.entity.ReviewPhoto;
import com.cafehub.cafehub.reviewPhoto.repository.ReviewPhotoRepository;
import com.cafehub.cafehub.reviewPhoto.response.PhotoUrlResponse;
import com.cafehub.cafehub.s3.S3Manager;
import com.cafehub.cafehub.security.UserDetailsImpl;
import com.cafehub.cafehub.security.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MyPageService {
//    @Value("${cloud.aws.s3.bucket}")
//    private String bucket;
//    private final AmazonS3Client s3Client;

    private final JwtProvider jwtProvider;
    private final ReviewRepository reviewRepository;
    private final ReviewPhotoRepository reviewPhotoRepository;
    private final LikeReviewRepository likeReviewRepository;
    private final CommentRepository commentsRepository;
    private final S3Manager s3Manager;


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
        Page<Review> allMyReviews = reviewRepository.findAllByMemberId(pageable, member.getId());
        List<Review> reviews = allMyReviews.getContent();
        List<ReviewResponse> myReviewList = makeReviewResponse(reviews);

        ProfileReviewsResponseDto profileReviewsResponseDto = ProfileReviewsResponseDto.builder()
                .reviewList(myReviewList)
                .isLast(allMyReviews.isLast())
                .currentPage(allMyReviews.getNumber())
                .build();

        return ResponseDto.success(profileReviewsResponseDto);
    }

    public ResponseDto<?> getMyComments(Pageable pageable, HttpServletRequest request) {
        Member member = getMemberFromJwt(request);
        Page<Comment> allMyComments = commentsRepository.findAllByMemberId(pageable, member.getId());
        List<Comment> comments = allMyComments.getContent();
        List<CommentResponseDTO> myCommentList = makeCommentResponse(comments);

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

    public ResponseDto<?> changeMyProfile(HttpServletRequest request, ProfileRequestDto requestDto, MultipartFile photo) {
        Member member = getMemberFromJwt(request);
        String nickname = requestDto.getNickname();

        System.out.println("포토 : " + photo==null);


        if (nickname != null) {
            member.updateNickname(nickname);
        }
        if (photo != null) {
//                String userPhotoUrl = uploadS3(profileImg, member);
//                member.updateProfileImg(userPhotoUrl);

            String photoKey = member.getUserPhotoKey();
            System.out.println(photoKey);
            if(photoKey!=null) {
                s3Manager.deleteFile(photoKey);
            }

            String userPhotoKey = s3Manager.generateProfilePhotoKeyName();
            String userPhotoUrl = s3Manager.uploadFile(userPhotoKey,photo);

            member.updateProfileImg(userPhotoUrl,userPhotoKey);
//        try {
//            if (nickname != null) {
//                member.updateNickname(nickname);
//            }
//            if (profileImg != null) {
////                String userPhotoUrl = uploadS3(profileImg, member);
////                member.updateProfileImg(userPhotoUrl);
//
//                member.updateProfileImg(userPhotoUrl,userPhotoKey);
//            }
//            return ResponseDto.success("Profile Changed");
//        } catch (IOException e) {
//            log.error(e.getMessage());
//            throw new FailedChangeProfile(ErrorCode.FAILED_CHANGE_PROFILE);
        }

        return ResponseDto.success("Profile Changed");
    }

    private List<ReviewResponse> makeReviewResponse(List<Review> reviews) {
        List<ReviewResponse> responseDtoList = new ArrayList<>();

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
            ReviewResponse responseDto = ReviewResponse.builder()
                    .reviewId(review.getId())
                    .author(review.getMember().getNickname())
                    .reviewRating(review.getRating())
                    .reviewContent(review.getContent())
                    .reviewCreateAt(review.getCreatedAt())
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

    private List<CommentResponseDTO> makeCommentResponse(List<Comment> comments) {
        List<CommentResponseDTO> responseDtoList = new ArrayList<>();

        for (Comment comment : comments) {
            CommentResponseDTO responseDto = CommentResponseDTO.builder()
                    .commentId(comment.getId())
                    .nickname(comment.getMember().getNickname())
                    .commentContent(comment.getContent())
                    .commentDate(comment.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                    .commentManagement(true) //
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
//
//    private String uploadS3(MultipartFile profileImg, Member member) throws IOException {
//        String userPhotoUrl = member.getUserPhotoUrl();
//        String basicImg = "아마존S3주소";
//        if (userPhotoUrl!=null && !userPhotoUrl.equals(basicImg)) {
//            fileDelete(userPhotoUrl);
//        }
//        String s3FileName = UUID.randomUUID() + "-" + profileImg.getOriginalFilename();
//        ObjectMetadata objMeta = new ObjectMetadata();
//        objMeta.setContentLength(profileImg.getSize());
//        objMeta.setContentType(profileImg.getContentType());
//        s3Client.putObject(bucket, s3FileName, profileImg.getInputStream(), objMeta);
//        return s3Client.getUrl(bucket, s3FileName).toString();
//
//    }

//    private void fileDelete(String userPhotoUrl) {
//        try {
//            String decodeVal = URLDecoder.decode(userPhotoUrl.substring(51), StandardCharsets.UTF_8);
//            s3Client.deleteObject(this.bucket,decodeVal);
//        } catch (AmazonServiceException e) {
//            log.error(e.getErrorMessage());
//        }
//    }
}
