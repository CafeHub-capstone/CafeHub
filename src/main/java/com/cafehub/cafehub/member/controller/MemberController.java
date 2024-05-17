package com.cafehub.cafehub.member.controller;

import com.cafehub.cafehub.common.dto.ResponseDto;
import com.cafehub.cafehub.member.oauth.service.KaKaoMemberService;
import com.cafehub.cafehub.member.service.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final KaKaoMemberService kaKaoMemberService;

    @GetMapping("/api/member/login")
    public ResponseEntity<?> redirectKakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        kaKaoMemberService.kakaoLogin(code, response);
        return ResponseEntity.ok().body(ResponseDto.success("Kakao OAuth Success"));
    }

    @PostMapping("/api/auth/reissue")
    public ResponseEntity<?> reissueJwt(HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.ok().body(memberService.reissueJwt(request, response));
    }

    @PostMapping("/api/auth/member/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok().body(memberService.logout());
    }
}
