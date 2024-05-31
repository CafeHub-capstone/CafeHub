package com.cafehub.cafehub.member.controller;

import com.cafehub.cafehub.common.dto.ResponseDto;
import com.cafehub.cafehub.member.oauth.service.KaKaoMemberService;
import com.cafehub.cafehub.member.service.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final KaKaoMemberService kaKaoMemberService;

    @GetMapping("/api/member/login")
    public void kakaoLogin(HttpServletResponse response) throws IOException {
        kaKaoMemberService.kakaoRedirct(response);
    }

    @GetMapping("/api/oauth")
    public ResponseEntity<?> redirectKakaoLogin(@RequestParam("code") String code, HttpServletResponse response, HttpServletRequest request) throws URISyntaxException, JsonProcessingException {
        log.info("Received OAuth code: {}", code);
        kaKaoMemberService.kakaoLogin(code, response);
        String redirectUri = request.getHeader("referer");
        if (redirectUri == null || redirectUri.isEmpty()) {
            redirectUri = "http://localhost:3000/";
        }
        return ResponseEntity.ok().location(new URI(redirectUri)).build();
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
