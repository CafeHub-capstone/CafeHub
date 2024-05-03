package com.cafehub.cafehub.member.exception;

import com.cafehub.cafehub.common.ErrorCode;

public class InvalidToken extends RuntimeException{
    private ErrorCode errorCode;

    public InvalidToken(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
