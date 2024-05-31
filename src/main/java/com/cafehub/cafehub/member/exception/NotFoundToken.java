package com.cafehub.cafehub.member.exception;

import com.cafehub.cafehub.common.ErrorCode;

public class NotFoundToken extends RuntimeException{
    private ErrorCode errorCode;

    public NotFoundToken(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
