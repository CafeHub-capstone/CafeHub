package com.cafehub.cafehub.comment.response;

import com.cafehub.cafehub.comment.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetAllCommentResponseDTO {

    private List<CommentResponseDTO> comments;

    private Boolean isLast;

    private Integer currentPage;
}
