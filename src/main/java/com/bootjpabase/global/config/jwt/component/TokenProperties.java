package com.bootjpabase.global.config.jwt.component;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties("jwt")
public class TokenProperties {

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.access-token-secret-key}")
    private String accessTokenSecretKey;

    @Value("${jwt.access-token-expiration}")
    private Long accessTokenExpiration;

    @Value("${jwt.refresh-token-secret-key}")
    private String refreshTokenSecretKey;

    @Value("${jwt.refresh-token-expiration}")
    private Long refreshTokenExpiration;
}
