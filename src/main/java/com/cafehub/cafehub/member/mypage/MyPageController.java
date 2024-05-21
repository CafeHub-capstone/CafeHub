package com.cafehub.cafehub.member.mypage;

import com.cafehub.cafehub.common.ErrorCode;
import com.cafehub.cafehub.common.dto.ResponseDto;
import com.cafehub.cafehub.member.mypage.dto.ProfileRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestController
@ControllerAdvice
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    @GetMapping("/api/auth/mypage")
    public ResponseDto<?> getMyProfile(HttpServletRequest request) {
        return ResponseDto.success(myPageService.getMyProfile(request));
    }

    @PostMapping("/api/auth/mypage")
    public ResponseEntity<?> changeProfile(HttpServletRequest request, @ModelAttribute @Validated ProfileRequestDto profileRequestDto) {
        return ResponseEntity.ok().body(myPageService.changeMyProfile(request, profileRequestDto));
    }

    @ExceptionHandler({SizeLimitExceededException.class, MaxUploadSizeExceededException.class})
    protected ResponseEntity<?> MaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        return ResponseEntity.badRequest().body(ResponseDto.fail(ErrorCode.FILE_SIZE_EXCEED));
    }
}
