package com.cafehub.cafehub.comment.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponseDTO {

    private Long commentId;

    private String nickname;

    private String commentContent;

    private LocalDateTime commentDate;

    private Boolean commentManagement;
}
