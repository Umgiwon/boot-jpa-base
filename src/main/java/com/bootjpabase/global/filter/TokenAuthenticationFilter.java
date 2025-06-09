package com.bootjpabase.global.filter;

import com.bootjpabase.global.config.jwt.component.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 토큰 관련 - filter
 */
@Component
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");
        String token = tokenProvider.getAccessToken(authorizationHeader);

        try {
            if (token != null && tokenProvider.validateToken(token)) {
                Authentication authentication = tokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            filterChain.doFilter(request, response);
        } catch (com.bootjpabase.global.exception.BusinessException e) {
            // TokenProvider에서 이미 처리된 비즈니스 예외
            throw e;
        } catch (Exception e) {
            // 예외 로깅
            logger.error("JWT 토큰 처리 중 오류 발생", e);
            // 더 구체적인 예외 발생
            throw new com.bootjpabase.global.exception.BusinessException(
                    com.bootjpabase.global.enums.common.ApiReturnCode.UNAUTHORIZED_ERROR);
        }
    }
}
