package com.cafehub.cafehub.member.oauth.service;

import com.cafehub.cafehub.member.entity.Member;
import com.cafehub.cafehub.member.oauth.dto.KaKaoMemberInfoDto;
import com.cafehub.cafehub.member.repository.MemberRepository;
import com.cafehub.cafehub.security.UserDetailsImpl;
import com.cafehub.cafehub.security.jwt.JwtProvider;
import com.cafehub.cafehub.security.jwt.RefreshToken;
import com.cafehub.cafehub.security.jwt.RefreshTokenRepository;
import com.cafehub.cafehub.security.jwt.TokenDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Service
@Slf4j
public class KaKaoMemberService {

    @Value("${kakao.restapi-key}")
    private String kakaoKey;

    @Value("${kakao.redirect-url}")
    private String kakaoRedirectUrl;

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProvider jwtProvider;

    public void kakaoRedirct(HttpServletResponse response) throws IOException {
        String encodeRedirectUrl = URLEncoder.encode(kakaoRedirectUrl, StandardCharsets.UTF_8);
        String redirectUrl = "https://kauth.kakao.com/oauth/authorize?client_id="
                + kakaoKey + "&redirect_uri=" + encodeRedirectUrl + "&response_type=code";
       log.info("Redirection to: {}", redirectUrl);
        response.sendRedirect(redirectUrl);
    }

    public void kakaoLogin(String code, HttpServletResponse response) throws JsonProcessingException {

        String accessToken = getAccessToken(code);
        KaKaoMemberInfoDto kaKaoMemberInfo = getKakaoMemberInfo(accessToken);

        Member kakaoUser = registerKakaoUser(kaKaoMemberInfo);

        forceLogin(kakaoUser);

        String refreshToken = kakaoMembersAuthorizationInput(kakaoUser, response);

        refreshTokenRepository.save(new RefreshToken(kakaoUser, refreshToken));
    }

    /**
     * 인가 코드로 액세스 토큰 요청
     */
    private String getAccessToken(String code) throws JsonProcessingException {

        WebClient webClient = WebClient.builder()
                .baseUrl("https://kauth.kakao.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();

        String response = webClient.post()
                .uri("/oauth/token")
                .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                        .with("client_id", kakaoKey)
                        .with("redirect_uri", kakaoRedirectUrl)
                        .with("code", code))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response);
        return jsonNode.get("access_token").asText();
    }

    /**
     * 토큰으로 카카오 API 호출
     */
    private KaKaoMemberInfoDto getKakaoMemberInfo(String accessToken) throws JsonProcessingException {

        WebClient webClient = WebClient.builder()
                .baseUrl("https://kapi.kakao.com")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();

        String response = webClient.post()
                .uri("/v2/user/me")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response);
        String nickname = jsonNode.get("properties").get("nickname").asText();
        String email = jsonNode.get("kakao_account").get("email").asText();

        return new KaKaoMemberInfoDto(nickname, email);
    }

    /**
     * 카카오 사용자 정보 -> 도메인 Member
     */
    private Member registerKakaoUser(KaKaoMemberInfoDto kaKaoMemberInfo) {
        String kaKaoEmail = kaKaoMemberInfo.getEmail();
        Member kakaoMember = memberRepository.findByEmail(kaKaoEmail).orElse(null);

        if (kakaoMember == null) {
            String nickname = kaKaoMemberInfo.getNickname();
            kakaoMember = new Member(kaKaoEmail, nickname);
            memberRepository.save(kakaoMember);
        }

        return kakaoMember;
    }

    /**
     * OAuth 로그인 시에 외부 서비스(예: 카카오)에서 받아온 사용자 정보를 기반으로 로그인 처리를 수행
     */
    private void forceLogin(Member kakaoMember) {
        UserDetails userDetails = new UserDetailsImpl(kakaoMember);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * OAuth 인증 후에 클라이언트에게 토큰을 제공하고, 클라이언트가 이를 사용하여 인증 및 권한 부여를 수행할 수 있도록 하는 데 사용
     */
    private String kakaoMembersAuthorizationInput(Member kakaoUser, HttpServletResponse response) {
        TokenDto tokenDto = jwtProvider.generateTokenDto(kakaoUser);
        response.addHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
        response.addHeader("refresh-token", tokenDto.getRefreshToken());
        return tokenDto.getRefreshToken();
    }
}
