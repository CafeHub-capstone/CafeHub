package com.cafehub.cafehub.member.mypage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponseDto {
    private String nickname;
    private String email;
    private String profileImg;
}
