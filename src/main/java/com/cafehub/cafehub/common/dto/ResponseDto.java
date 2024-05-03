package com.cafehub.cafehub.common.dto;

import com.cafehub.cafehub.common.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseDto<T> {
    private boolean success;
    private T data;
    private ErrorCode errorcode;

    public static <T> ResponseDto<T> success(T data) {
        return new ResponseDto<>(true, data, null);
    }

    public static <T> ResponseDto<T> fail(ErrorCode errorcode) {
        return new ResponseDto<>(false, null, errorcode);
    }

}
