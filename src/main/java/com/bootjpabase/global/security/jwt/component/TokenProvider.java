package com.bootjpabase.global.security.jwt.component;

import com.bootjpabase.api.token.domain.dto.response.TokenResponseDTO;
import com.bootjpabase.api.user.domain.entity.User;
import com.bootjpabase.global.enums.common.ApiReturnCode;
import com.bootjpabase.global.enums.user.TokenType;
import com.bootjpabase.global.exception.BusinessException;
import com.bootjpabase.global.security.jwt.domain.SecurityUser;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class TokenProvider {

    private static final String BEARER_PREFIX = "Bearer ";
    private final TokenProperties tokenProperties;

    private Key accessKey;
    private Key refreshKey;

    @PostConstruct
    private void init() {
        this.accessKey = Keys.hmacShaKeyFor(tokenProperties.getAccessTokenSecretKey().getBytes());
        this.refreshKey = Keys.hmacShaKeyFor(tokenProperties.getRefreshTokenSecretKey().getBytes());
    }

    /**
     * access & refresh 토큰 생성하여 dto return
     *
     * @param user 사용자 정보
     * @return 생성된 토큰 응답 dto
     */
    public TokenResponseDTO createAllToken(User user) {
        return TokenResponseDTO.builder()
                .accessToken(createToken(user, TokenType.ACCESS))
                .refreshToken(createToken(user, TokenType.REFRESH))
                .build();
    }

    /**
     * 토큰 생성
     *
     * @param user      사용자 정보
     * @param tokenType 토큰 타입 (access 또는 refresh)
     * @return 생성된 토큰 문자열
     */
    public String createToken(User user, TokenType tokenType) {
        Date date = new Date();

        boolean isAccessToken = TokenType.ACCESS.equals(tokenType);
        long expiration = isAccessToken
                ? tokenProperties.getAccessTokenExpiration()
                : tokenProperties.getRefreshTokenExpiration();

        Key signingKey = isAccessToken ? accessKey : refreshKey;

        return Jwts.builder()
                .setSubject(user.getUserId())
                .setExpiration(new Date(date.getTime() + expiration))
                .setIssuedAt(date)
                .claim("userSn", user.getUserSn())
                .claim("userId", user.getUserId())
                .claim("userName", user.getUserName())
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 헤더에서 토큰 추출
     *
     * @param authorizationHeader 인증 헤더 문자열
     * @return 추출된 액세스 토큰
     */
    public String getAccessToken(String authorizationHeader) {
        return (authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX))
                ? authorizationHeader.substring(BEARER_PREFIX.length())
                : null;
    }

    /**
     * 토큰에서 authentication 추출
     *
     * @param token 토큰 문자열
     * @return 인증 객체
     */
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);

        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

        SecurityUser user = new SecurityUser(this.getIdFromToken(token), (String) this.getClaims(token).get("userName"), "", claims);

        return new UsernamePasswordAuthenticationToken(user, token, authorities);
    }

    /**
     * 토큰에서 claims 추출
     *
     * @param token 토큰 문자열
     * @return 토큰의 클레임 정보
     */
    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(accessKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 토큰에서 id 추출
     *
     * @param token 토큰 문자열
     * @return 토큰에서 추출한 사용자 ID
     */
    public String getIdFromToken(String token) {
        return getClaims(token).getSubject();
    }

    /**
     * 토큰에서 name 추출
     *
     * @param token 토큰 문자열
     * @return 토큰에서 추출한 사용자 이름
     */
    public String getNameFromToken(String token) {
        return (String) getClaims(token).get("userName");
    }

    /**
     * 토큰 검증
     *
     * @param token 검증할 토큰 문자열
     */
    public void validateToken(String token, TokenType tokenType) {
        try {

            Key signingKey = TokenType.ACCESS.equals(tokenType) ? accessKey : refreshKey;

            Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            throw new BusinessException(ApiReturnCode.EXPIRED_TOKEN_ERROR);
        } catch (UnsupportedJwtException e) {
            throw new BusinessException(ApiReturnCode.UNSUPPORTED_TOKEN_ERROR);
        } catch (MalformedJwtException e) {
            throw new BusinessException(ApiReturnCode.MALFORMED_TOKEN_ERROR);
        } catch (SignatureException e) {
            throw new BusinessException(ApiReturnCode.INVALID_SIGNATURE_ERROR);
        } catch (IllegalArgumentException e) {
            throw new BusinessException(ApiReturnCode.UNAUTHORIZED_ERROR);
        }
    }

    /**
     * 토큰의 로그인 정보
     *
     * @return 현재 인증된 사용자 정보
     */
    public SecurityUser getUserLoginInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return (SecurityUser) authentication.getPrincipal();
    }
}
