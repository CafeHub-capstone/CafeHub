package com.cafehub.cafehub.comment.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetAllCommentRequestDTO {

    private Long reviewId;

    private Integer currentPage;
}
