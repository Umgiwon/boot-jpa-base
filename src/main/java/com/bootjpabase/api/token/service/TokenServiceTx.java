package com.bootjpabase.api.token.service;

import com.bootjpabase.api.user.domain.entity.User;
import com.bootjpabase.api.token.domain.entity.RefreshToken;
import com.bootjpabase.api.token.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TokenServiceTx {

    private final TokenRepository tokenRepository;

    /**
     * 토큰 저장
     * @param refreshToken
     * @param user
     */
    public void saveRefreshToken(String refreshToken, User user) {

        // 토큰 entity 생성
        RefreshToken saveRefreshToken = createRefreshTokenEntity(refreshToken, user);

        // refresh 토큰 저장
        tokenRepository.save(saveRefreshToken);
    }

    /**
     * 토큰 entity 생성
     * @param refreshToken
     * @param user
     * @return
     */
    private RefreshToken createRefreshTokenEntity(String refreshToken, User user) {
        return RefreshToken.builder()
                .token(refreshToken)
                .user(user)
                .build();
    }
}
