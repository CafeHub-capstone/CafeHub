package com.cafehub.cafehub.member.exception;

import com.cafehub.cafehub.common.ErrorCode;

public class ExpiredJwtToken extends RuntimeException {
    private ErrorCode errorCode;

    public ExpiredJwtToken(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

}
