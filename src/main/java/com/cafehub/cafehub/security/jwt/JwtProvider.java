package com.cafehub.cafehub.security.jwt;

import com.cafehub.cafehub.member.entity.Member;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;


@Slf4j
@Component
@RequiredArgsConstructor
/**
 * JWT를 생성 및 유효성 검사
 * JWT를 사용하여 사용자를 인증
 */
public class JwtProvider {

    private final UserDetailsService userDetailsService;

    @Value("${jwt.secret}")
    private String JWT_SECRET;
    private Key key;

    /**
     * 키 초기화
     */
    @PostConstruct
    protected void keyInit() {
        key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes());
    }

    /**
     * 주어진 회원 정보를 이용하여 엑세스 토큰과 리프레시 토큰을 생성
     */
    public TokenDto generateTokenDto(Member member) {
        long now = new Date().getTime();

        int ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30; //30분
        String accessToken = Jwts.builder()
                .setSubject(member.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(now + ACCESS_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        int REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 14; //2주일
        String refreshToken = Jwts.builder()
                .setSubject(member.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return new TokenDto(accessToken, refreshToken);
    }

    /**
     * 토큰의 유효성 검사
     */
    public boolean isValidToken(String token) {
        if (token == null) {
            log.error("JWT token is null");
            return false;
        }

        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJwt(token);
            return true;
        } catch (ExpiredJwtException e) {
          log.error("JWT token is expired");
        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty");
        } catch (JwtException e) {
            log.error("JWT token parsing error: {}", e.getMessage());
        }
        return false;
    }

    /**
     * 사용자의 인증 정보 find
     */
    public Authentication getAuthentication(String token) {
        String email = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJwt(token).getBody().getSubject();
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

}
