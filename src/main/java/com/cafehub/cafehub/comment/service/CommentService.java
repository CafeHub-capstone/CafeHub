package com.cafehub.cafehub.comment.service;

import com.cafehub.cafehub.comment.request.CreateCommentRequestDTO;
import com.cafehub.cafehub.comment.request.DeleteCommentRequestDTO;
import com.cafehub.cafehub.comment.request.GetAllCommentRequestDTO;
import com.cafehub.cafehub.common.dto.ResponseDto;
import jakarta.servlet.http.HttpServletRequest;

public interface CommentService {

    ResponseDto<?> getAllComment(GetAllCommentRequestDTO getAllCommentRequestDTO, HttpServletRequest request);

    ResponseDto<?> createComment(CreateCommentRequestDTO createCommentRequestDTO);

    ResponseDto<?> deleteComment(DeleteCommentRequestDTO deleteCommentRequestDTO);
}
