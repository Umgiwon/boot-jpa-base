package com.bootjpabase.global.security.jwt.component;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "jwt")
public class TokenProperties {

    private String issuer;
    private String accessTokenSecretKey;
    private Long accessTokenExpiration;
    private String refreshTokenSecretKey;
    private Long refreshTokenExpiration;
}
