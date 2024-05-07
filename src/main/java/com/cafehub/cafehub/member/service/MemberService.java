package com.cafehub.cafehub.member.service;

import com.cafehub.cafehub.common.ErrorCode;
import com.cafehub.cafehub.common.dto.ResponseDto;
import com.cafehub.cafehub.member.entity.Member;
import com.cafehub.cafehub.member.exception.ExpiredJwtToken;
import com.cafehub.cafehub.member.exception.InvalidToken;
import com.cafehub.cafehub.member.exception.NotFoundToken;
import com.cafehub.cafehub.member.exception.NotMatchToken;
import com.cafehub.cafehub.security.UserDetailsImpl;
import com.cafehub.cafehub.security.jwt.JwtProvider;
import com.cafehub.cafehub.security.jwt.RefreshToken;
import com.cafehub.cafehub.security.jwt.RefreshTokenRepository;
import com.cafehub.cafehub.security.jwt.TokenDto;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProvider jwtProvider;

    /**
     * JWT를 재발급하는 메서드
     * 1. 클라이언트로부터 온 HTTP 요청의 헤더에서 "refresh-token"을 가져옴
     * 2. JWT 만료된 경우 해당 토큰을 데이터베이스에서 삭제 및 ErrorDTO 전송
     * 3. JWT 유효하지 않을 경우 ErrorDTO전송
     * 4. 유효한 경우, 토큰이 저장된 멤버를 찾기
     * 5. 해당 멤버의 닉네임으로 데이터베이스에서 저장된 "refresh-token" find
     * 6. - (1) 데이터베이스에 해당 토큰이 없거나
     * 6. - (2) 클라이언트가 제공한 "refresh-token"이 일치 하지 않으면 ErrorDTO 전송
     * 7. 새로운 액세스 토큰과 리프레시 토큰을 생성하고 응답 헤더에 추가
     * 8. 데이터베이스에 해당 토큰 저장
     */
    @Transactional
    public ResponseDto<?> reissueJwt(HttpServletRequest request, HttpServletResponse response) {

        String refreshToken = request.getHeader("refresh-token");
        Member member = findMemberFromRefreshToken(refreshToken);
        RefreshToken tokenFromDB = validateRefreshToken(member, refreshToken);

        TokenDto tokenDto = jwtProvider.generateTokenDto(member);
        addTokenToResponseHeaders(response, tokenDto);

        tokenFromDB.updateToken(tokenDto.getRefreshToken());

        return ResponseDto.success("reissue-success");

    }

    private Member findMemberFromRefreshToken(String refreshToken) {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) jwtProvider.getAuthentication(refreshToken).getPrincipal();
            return userDetails.getMember();
        } catch (ExpiredJwtException e) {
            refreshTokenRepository.deleteByToken(refreshToken);

            //두 방식 중 어떤 것이 더 좋을지 고민을 하고 추후 결정하려고 함.
//            return ResponseDto.fail(ErrorCode.REFRESH_TOKEN_EXPIRED);
            throw  new ExpiredJwtToken(ErrorCode.REFRESH_TOKEN_EXPIRED);
        } catch (Exception e) {
//            return ResponseDto.fail(ErrorCode.INVALID_TOKEN);
            throw new InvalidToken(ErrorCode.INVALID_TOKEN);
        }
    }

    private RefreshToken validateRefreshToken(Member member, String refreshToken) {
        RefreshToken tokenFromDB = refreshTokenRepository.findById(member.getEmail()).orElse(null);

        if (tokenFromDB == null) {
            throw new NotFoundToken(ErrorCode.REFRESH_TOKEN_NOT_FOUND);
        } else if (!refreshToken.equals(tokenFromDB.getToken())) {
            throw new NotMatchToken(ErrorCode.REFRESH_TOKEN_NOT_ALLOWED);
        }

        return tokenFromDB;
    }

    private void addTokenToResponseHeaders(HttpServletResponse response, TokenDto tokenDto) {
        response.addHeader("Authorization", "BEARER " + tokenDto.getAccessToken());
        response.addHeader("refresh-token", tokenDto.getRefreshToken());
    }

    public ResponseDto<?> logout() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        refreshTokenRepository.deleteById(userDetails.getMember().getEmail());
        return ResponseDto.success("logout success");
    }

}
