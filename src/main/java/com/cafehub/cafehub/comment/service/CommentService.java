package com.cafehub.cafehub.comment.service;

import com.cafehub.cafehub.comment.request.CreateCommentRequestDTO;
import com.cafehub.cafehub.comment.request.DeleteCommentRequestDTO;
import com.cafehub.cafehub.comment.request.GetAllCommentRequestDTO;
import com.cafehub.cafehub.comment.response.CreateCommentResponseDTO;
import com.cafehub.cafehub.comment.response.DeleteCommentResponseDTO;
import com.cafehub.cafehub.common.dto.ResponseDto;
import org.springframework.transaction.annotation.Transactional;

public interface CommentService {

    ResponseDto<?> getAllComment(GetAllCommentRequestDTO getAllCommentRequestDTO);

    ResponseDto<?> createComment(CreateCommentRequestDTO createCommentRequestDTO);

    ResponseDto<?> deleteComment(DeleteCommentRequestDTO deleteCommentRequestDTO);
}
