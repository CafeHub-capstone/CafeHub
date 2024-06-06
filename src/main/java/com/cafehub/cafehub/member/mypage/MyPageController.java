package com.cafehub.cafehub.member.mypage;

import com.cafehub.cafehub.common.ErrorCode;
import com.cafehub.cafehub.common.dto.ResponseDto;
import com.cafehub.cafehub.member.mypage.dto.ProfileRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

@RestController
@ControllerAdvice
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    @GetMapping("/api/auth/mypage")
    public ResponseEntity<?> getProfile(HttpServletRequest request) {
        return ResponseEntity.ok().body(myPageService.getMyProfile(request));
    }

    @GetMapping("/api/auth/mypage/reviews")
    public ResponseEntity<?> getProfileReviews(@PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable, HttpServletRequest request) {
        return ResponseEntity.ok().body(myPageService.getMyReviews(pageable, request));
    }

    @GetMapping("/api/auth/mypage/comments")
    public ResponseEntity<?> geyProfileComments(@PageableDefault(page = 0, size = 3, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable, HttpServletRequest request) {
        return ResponseEntity.ok().body(myPageService.getMyComments(pageable, request));
    }

    @PostMapping(value = "/api/auth/mypage", consumes = {"multipart/form-data"})
    public ResponseEntity<?> changeProfile(HttpServletRequest request,
                                           @RequestPart ("nickname") ProfileRequestDto profileRequestDto,
                                           @RequestPart ("profileImg") MultipartFile photo) {

        return ResponseEntity.ok().body(myPageService.changeMyProfile(request, profileRequestDto,photo));
    }

    @ExceptionHandler({SizeLimitExceededException.class, MaxUploadSizeExceededException.class})
    protected ResponseEntity<?> MaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        return ResponseEntity.badRequest().body(ResponseDto.fail(ErrorCode.FILE_SIZE_EXCEED));
    }
}
