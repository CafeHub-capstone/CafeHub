package com.cafehub.cafehub.comment.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateCommentRequestDTO {
    // /api/auth/reviews/{reviewId}/comment

    private Long reviewId;

    private String commentContent;
}
