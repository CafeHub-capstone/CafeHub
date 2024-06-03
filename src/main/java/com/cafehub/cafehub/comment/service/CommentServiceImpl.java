package com.cafehub.cafehub.comment.service;

import com.cafehub.cafehub.comment.entity.Comment;
import com.cafehub.cafehub.comment.repository.CommentRepository;
import com.cafehub.cafehub.comment.request.CreateCommentRequestDTO;
import com.cafehub.cafehub.comment.request.DeleteCommentRequestDTO;
import com.cafehub.cafehub.comment.request.GetAllCommentRequestDTO;
import com.cafehub.cafehub.comment.response.CommentResponseDTO;
import com.cafehub.cafehub.comment.response.GetAllCommentResponseDTO;
import com.cafehub.cafehub.common.dto.ResponseDto;
import com.cafehub.cafehub.member.entity.Member;
import com.cafehub.cafehub.member.repository.MemberRepository;
import com.cafehub.cafehub.review.entity.Review;
import com.cafehub.cafehub.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService{

    private static final int COMMENT_PAGING_SIZE = 4;

    private final MemberRepository memberRepository;

    private final ReviewRepository reviewRepository;

    private final CommentRepository commentRepository;

    @Override
    public ResponseDto<?> getAllComment(GetAllCommentRequestDTO request) {

        String currentMemberEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        // 슬라이스 처리를 위해 슬라이스 생성.
        Slice<Comment> commentSlice = commentRepository.findAllByReviewIdWithMember(request.getReviewId(),
                PageRequest.of(request.getCurrentPage(), COMMENT_PAGING_SIZE, Sort.by(Sort.Direction.DESC, "createdAt")));

        List<CommentResponseDTO> comments = commentSlice.getContent().stream().map(comment -> {
            return CommentResponseDTO.builder()
                    .commentId(comment.getId())
                    .nickname(comment.getMember().getNickname())
                    .commentContent(comment.getContent())
                    .commentDate(comment.getCreatedAt())
                    .commentManagement(comment.getMember().getEmail().equals(currentMemberEmail))
                    .build();
        }).collect(Collectors.toList());

        GetAllCommentResponseDTO response = new GetAllCommentResponseDTO(comments, commentSlice.isLast(), commentSlice.getNumber());

        return ResponseDto.success(response);
    }

    @Override
    @Transactional
    public ResponseDto<?> createComment(CreateCommentRequestDTO request) {

        // 현재 로그인한 멤버가 필요. 댓글 고유번호는 자동. 리뷰 고유번호, 내용.
        String currentMemberEmail=SecurityContextHolder.getContext().getAuthentication().getName(); // 시큐리티 컨텍스트
        // JWT 액세스 토큰을 가져와서, 로그인에 성공하면 어센시케이션 객체를 임시 세션에 저장함.
        // 그런데 어센시케이션 객체를 통해 JWT 토큰 안의 내용을 가져올 수 있음.

        Member loginMember = memberRepository.findByEmail(currentMemberEmail).get();
        Review currentReview = reviewRepository.findById(request.getReviewId()).get();

        Comment comment = Comment.builder()
                .content(request.getCommentContent())
                .member(loginMember)
                .review(currentReview)
                .build();

        commentRepository.save(comment);

        currentReview.updateCommentCount(currentReview.getCommentCount()+1);

        return  ResponseDto.success(comment.getId());
    }

    @Override
    @Transactional
    public ResponseDto<?> deleteComment(DeleteCommentRequestDTO request) {

//        // 현재 로그인한 멤버가 댓글의 작성자인지 비교하는 로직 필요.
//        String currentMemberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
//        Member loginMember = memberRepository.findByEmail(currentMemberEmail).get();
//
        Comment comment = commentRepository.findById(request.getCommentId()).get();
//        comment.getMember().getId().equals(loginMember.getId());

        commentRepository.deleteById(request.getCommentId());

        Review currentReview = comment.getReview(); //
        currentReview.updateCommentCount(currentReview.getCommentCount()-1);

        return ResponseDto.success(comment.getId());
    }


}
