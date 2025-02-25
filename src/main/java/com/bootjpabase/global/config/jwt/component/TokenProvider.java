package com.bootjpabase.global.config.jwt.component;

import com.nangman.api.admin.system.manager.domain.entity.Manager;
import com.nangman.global.config.jwt.domain.SecurityManager;
import com.nangman.global.enums.common.ApiReturnCode;
import com.nangman.global.exception.BusinessException;
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
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class TokenProvider {

    private final TokenProperties tokenProperties;
    private Key key;

    private static final String BEARER_PREFIX = "Bearer ";

    @PostConstruct
    private void init() {
        this.key = Keys.hmacShaKeyFor(tokenProperties.getAccessTokenSecretKey().getBytes());
    }

    /**
     * 토큰 생성
     * @param manager
     * @return
     */
    public String createToken(Manager manager) {
        Date date = new Date();

        return Jwts.builder()
                .setSubject(manager.getId())
                .setExpiration(new Date(date.getTime() + tokenProperties.getAccessTokenExpiration()))
                .setIssuedAt(date)
                .claim("id", manager.getId())
                .claim("name", manager.getName())
                .claim("roleCode", manager.getRoleCode())
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 헤더에서 토큰 추출
     * @param authorizationHeader
     * @return
     */
    public String getAccessToken(String authorizationHeader) {
        return (authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX))
                ? authorizationHeader.substring(BEARER_PREFIX.length())
                : null;
    }

    /**
     * 토큰에서 authentication 추출
     * @param token
     * @return
     */
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);

        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

        SecurityManager manager = new SecurityManager(this.getIdFromToken(token), "", claims);

        return new UsernamePasswordAuthenticationToken(manager, token, authorities);
    }

    /**
     * 토큰에서 claims 추출
     * @param token
     * @return
     */
    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 토근에서 id 추출
     * @param token
     * @return
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
     * 토큰 검증
     * @param token
     * @return
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

}
