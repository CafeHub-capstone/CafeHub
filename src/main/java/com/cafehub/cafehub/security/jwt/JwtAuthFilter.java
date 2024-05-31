package com.cafehub.cafehub.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String path = request.getServletPath();
            if (path.startsWith("/api/auth/reissue")) {
                filterChain.doFilter(request, response);
            } else {
                String accessToken = getAccessTokenFromRequest(request);
                if (StringUtils.hasText(accessToken) && jwtProvider.isValidToken(accessToken)) {
                    Authentication auth = jwtProvider.getAuthentication(accessToken);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                } else {
                    request.setAttribute("INVALID_JWT", "INVALID_JWT");
                }
            }
        } catch (ExpiredJwtException e) {
            request.setAttribute("EXPIRED_JWT", "EXPIRED_JWT");
            log.error("Could not set user authentication in security context", e);
        } catch (NullPointerException e) {
            log.error("Could not set user authentication in security context", e);
        }
        filterChain.doFilter(request, response);
    }

    private String getAccessTokenFromRequest(HttpServletRequest request) {
        String accessToken = request.getHeader("Authorization");
        try {
            return accessToken.substring("Bearer ".length());
        } catch (NullPointerException e) {
            return null;
        }
    }
}
