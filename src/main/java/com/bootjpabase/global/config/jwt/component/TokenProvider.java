package com.bootjpabase.global.config.jwt.component;

import com.bootjpabase.api.token.domain.dto.TokenResponseDTO;
import com.bootjpabase.api.user.domain.entity.User;
import com.bootjpabase.global.config.jwt.domain.SecurityUser;
import com.bootjpabase.global.enums.common.ApiReturnCode;
import com.bootjpabase.global.exception.BusinessException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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

    private static final String ACCESS = "access";
    private static final String REFRESH = "refresh";
    private static final String BEARER_PREFIX = "Bearer ";
    private final TokenProperties tokenProperties;
    private Key key;

    private Key accessKey;
    private Key refreshKey;

    @PostConstruct
    private void init() {
        this.accessKey = Keys.hmacShaKeyFor(tokenProperties.getAccessTokenSecretKey().getBytes());
        this.refreshKey = Keys.hmacShaKeyFor(tokenProperties.getRefreshTokenSecretKey().getBytes());
        this.key = this.accessKey; // 기존 메소드와의 하위 호환성을 위함
    }

    /**
     * access & refresh 토큰 생성하여 dto return
     *
     * @param user 사용자 정보
     * @return 생성된 토큰 응답 dto
     */
    public TokenResponseDTO createAllToken(User user) {
        return TokenResponseDTO.builder()
                .accessToken(createToken(user, ACCESS))
                .refreshToken(createToken(user, REFRESH))
                .build();
    }

    /**
     * 토큰 생성
     *
     * @param user      사용자 정보
     * @param tokenType 토큰 타입 (access 또는 refresh)
     * @return 생성된 토큰 문자열
     */
    public String createToken(User user, String tokenType) {
        Date date = new Date();

        boolean isAccessToken = ACCESS.equals(tokenType);
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
                .claim("name", user.getUserName()) // 하위 호환성을 위한 name 클레임 추가
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

        SecurityUser user = new SecurityUser(this.getIdFromToken(token), (String) this.getClaims(token).get("name"), "", claims);

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
                .setSigningKey(key)
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
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * 토큰에서 name 추출
     *
     * @param token 토큰 문자열
     * @return 토큰에서 추출한 사용자 이름
     */
    public String getNameFromToken(String token) {
        Claims claims = getClaims(token);
        return (String) claims.get("userName");
    }

    /**
     * 토큰 검증
     *
     * @param token 검증할 토큰 문자열
     * @return 토큰 유효성 여부
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new BusinessException(ApiReturnCode.EXPIRED_TOKEN_ERROR);
        } catch (Exception e) {
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
