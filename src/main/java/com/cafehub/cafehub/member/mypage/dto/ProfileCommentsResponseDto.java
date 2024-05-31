package com.cafehub.cafehub.member.mypage.dto;

import com.cafehub.cafehub.comment.response.CommentResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 추후 리뷰 및 댓글 도메인과 병합시 해당 Dto는 삭제예정
 * 이유 : 해당 Dto는 마이페이지 댓글 모음과 댓글 도메인의 댓글 모임의 형태가 동일함
 * 즉, 두 개를 공존하여 사용할시 불필요한 클래스가 생성
 */
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileCommentsResponseDto {

    private List<CommentResponseDTO> commentList;

    private Boolean isLast;

    private Integer currentPage;
}
