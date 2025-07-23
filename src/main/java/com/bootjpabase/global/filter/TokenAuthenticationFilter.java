package com.bootjpabase.global.filter;

import com.bootjpabase.global.enums.common.ApiReturnCode;
import com.bootjpabase.global.enums.user.TokenType;
import com.bootjpabase.global.exception.BusinessException;
import com.bootjpabase.global.security.jwt.component.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 토큰 관련 - filter
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {

        String authorizationHeader = request.getHeader("Authorization");
        String token = tokenProvider.getAccessToken(authorizationHeader);

        try {
            if (token != null) {
                tokenProvider.validateToken(token, TokenType.ACCESS); // 토큰 검증
                Authentication authentication = tokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            filterChain.doFilter(request, response);
        } catch (BusinessException e) {
            log.error("JWT 인증 관련 비즈니스 예외 발생 : {}", e.getApiReturnCode().getMessage());
            request.setAttribute("BusinessException", e);

            throw e;
        } catch (Exception e) {
            log.error("JWT 토큰 처리 중 알 수 없는 예외 발생", e);
            throw new BusinessException(ApiReturnCode.SERVER_ERROR);
        }
    }
}
