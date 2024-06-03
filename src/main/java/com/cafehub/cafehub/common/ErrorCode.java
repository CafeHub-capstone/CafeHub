package com.cafehub.cafehub.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

// 에러 코드는 이곳에서 한번에 관리 한다.

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@AllArgsConstructor
@Getter
public enum ErrorCode {

    // refresh-token 만료
    REFRESH_TOKEN_EXPIRED("REFRESH_TOKEN_EXPIRED", "Refresh Token Expired"),
    // access-token 만료
    ACCESS_TOKEN_EXPIRED("ACCESS_TOKEN_EXPIRED", "Access Token Expired"),
    // 유효하지 않은 토큰
    INVALID_TOKEN("INVALID_TOKEN", "Invalid or No Token"),
    // refresh-token DB에 없음
    REFRESH_TOKEN_NOT_FOUND("REFRESH_TOKEN_NOT_FOUND", "No Such Refresh Token"),
    // DB의 refresh-token과 보낸 토큰이 다름
    REFRESH_TOKEN_NOT_ALLOWED("REFRESH_TOKEN_NOT_ALLOWED", "Refresh Token Different"),
    // 프로필 변경 실패
    FAILED_CHANGE_PROFILE("FAILED_CHANGE_PROFILE", "Failed Change Profile"),
    // 용량 초과

    FILE_SIZE_EXCEED("FILE_SIZE_EXCEED", "File Size Exceed. Limit is 1MB"),
    //요청 위치에 리소스 없음
    NULL_ID("NULL_ID", "Id Doesn't Exist"),
    //유저의 권한 없는 요청
    NOT_ALLOWED("NOT_ALLOWED", "Only The Writer Is Allowed"),

    //북마크에서 해당하는 카페를 찾을 수 없음
    CAFE_NOT_FOUND("CAFE_NOT_FOUND", "Cafe Not Found"),
    //요청하는 카페와 제공하는 카페가 불일치
    INVALID_CAFE_ID("INVALID_CAFE_ID","CafeId Different");

    private final String code;
    private final String message;
}