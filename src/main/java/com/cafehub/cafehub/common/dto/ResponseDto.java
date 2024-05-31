package com.cafehub.cafehub.common.dto;

import com.cafehub.cafehub.common.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 *    우리 프로젝트의 모든 DTO는
 *
 *    {
 *        success: ture | false
 *        Data
 *        errorMessage
 *    }
 *
 *    구조로 통일이 되어있다.
 *
 *    그래서 굳이 매번 ResponseDTO를 만들 때 마다 Data를 제외한 모든 부분을 다시 DTO로 만들어서 포장할 필요가 없다.
 *
 *
 *     public ResponseEntity<?> f() {
 *         return ResponseEntity.ok().body(ResponseDTO.success(DataResponse));
 *     }
 *
 *     이런식으로 사용해 줄 수가 있다.
 *     ResponseEntity를 사용하면 response의 HTTP 메세지 상태코드를 동적으로 결정 할 수 있다.
 *     이걸 결정하지 않으면 , 200, 40x, 500 이런식으로 단편적으로 결정 되어 나가지만
 *
 *     우리가 추후 프론트와 로그인 성공은 2001, 리스트 반환 성공은 2002 이런 API 규칙을 만들어서 구분지어 줄 수 있는 여지도 생기게 됨
 */


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
