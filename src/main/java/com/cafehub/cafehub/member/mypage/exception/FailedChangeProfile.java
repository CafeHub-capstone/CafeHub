package com.cafehub.cafehub.member.mypage.exception;

import com.cafehub.cafehub.common.ErrorCode;

public class FailedChangeProfile extends RuntimeException{
    private ErrorCode errorCode;

    public FailedChangeProfile(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
