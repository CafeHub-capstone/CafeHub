package com.cafehub.cafehub.likeReview.service;

import com.cafehub.cafehub.common.ErrorCode;
import com.cafehub.cafehub.common.dto.ResponseDto;
import com.cafehub.cafehub.likeReview.dto.LikeReviewRequestDto;
import com.cafehub.cafehub.likeReview.entity.LikeReview;
import com.cafehub.cafehub.likeReview.repository.LikeReviewRepository;
import com.cafehub.cafehub.member.entity.Member;
import com.cafehub.cafehub.review.entity.Review;
import com.cafehub.cafehub.security.UserDetailsImpl;
import com.cafehub.cafehub.security.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeReviewService {

    private final LikeReviewRepository likeReviewRepository;
    private final ReviewRepository reviewRepository;
    private final JwtProvider jwtProvider;

    public ResponseDto<?> updateLike(Long reviewId, LikeReviewRequestDto likeReviewRequestDto, HttpServletRequest request) {
        Member member = getMemberFromJwt(request);
        Review review = isPresentReview(reviewId);

        if (review == null) {
            return ResponseDto.fail(ErrorCode.NULL_ID);
        }

        Optional<LikeReview> like = likeReviewRepository.findByReviewAndMember(review, member);

        /**
         * 리뷰 좋아요 등록
         */
        if (like.isEmpty() && likeReviewRequestDto.getReviewChecked()) {
            LikeReview likeReview = LikeReview.builder()
                    .review(review)
                    .member(member)
                    .build();
            likeReviewRepository.save(likeReview);
        } else if (like.isPresent() && !likeReviewRequestDto.getReviewChecked()){
            likeReviewRepository.delete(like.get());
        }
        return ResponseDto.success("Update LikeReview Success");
    }

    public Member getMemberFromJwt(HttpServletRequest request) {
        Authentication authentication = jwtProvider.getAuthentication(request.getHeader("Authorization").substring(7));
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getMember();
    }

    private Review isPresentReview(Long reviewId) {
        Optional<Review> reviewOptional = reviewRepository.findById(reviewId);
        return reviewOptional.orElse(null);
    }
}
