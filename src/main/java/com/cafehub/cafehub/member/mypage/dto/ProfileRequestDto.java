package com.cafehub.cafehub.member.mypage.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProfileRequestDto {
    private String nickname;
    private Object profileImg;
}
