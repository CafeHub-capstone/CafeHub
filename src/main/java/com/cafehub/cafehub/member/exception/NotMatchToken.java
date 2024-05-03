package com.cafehub.cafehub.member.exception;

import com.cafehub.cafehub.common.ErrorCode;

public class NotMatchToken extends RuntimeException{
    private ErrorCode errorCode;

    public NotMatchToken(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
