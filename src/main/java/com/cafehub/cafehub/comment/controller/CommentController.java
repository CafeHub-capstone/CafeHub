package com.cafehub.cafehub.comment.controller;

import com.cafehub.cafehub.comment.request.CreateCommentRequestDTO;
import com.cafehub.cafehub.comment.request.DeleteCommentRequestDTO;
import com.cafehub.cafehub.comment.request.GetAllCommentRequestDTO;
import com.cafehub.cafehub.comment.response.CreateCommentResponseDTO;
import com.cafehub.cafehub.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/reviews/{reviewId}/comments/{currentPage}")
    public ResponseEntity<?> getAllComment(@PathVariable("reviewId") Long reviewId,
                                        @PathVariable("currentPage") Integer currentPage){

        GetAllCommentRequestDTO getAllCommentRequestDTO = new GetAllCommentRequestDTO(reviewId, currentPage);

        return ResponseEntity.ok().body(commentService.getAllComment(getAllCommentRequestDTO));
    }

    @PostMapping("/auth/reviews/{reviewId}/comment")
    public ResponseEntity<?> createComment(@PathVariable("reviewId") Long reviewId,
                                        @RequestBody CreateCommentRequestDTO createCommentRequestDTO){

        createCommentRequestDTO.setReviewId(reviewId);

        return ResponseEntity.ok().body(commentService.createComment(createCommentRequestDTO));
    }

    @PostMapping("/auth/reviews/{commentId}/delete")
    public ResponseEntity<?> deleteComment(@PathVariable("commentId") Long commentId){

        DeleteCommentRequestDTO deleteCommentRequestDTO = new DeleteCommentRequestDTO(commentId);

        return ResponseEntity.ok().body(commentService.deleteComment(deleteCommentRequestDTO));
    }
}
