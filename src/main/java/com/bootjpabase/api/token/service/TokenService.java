package com.bootjpabase.api.token.service;

import com.bootjpabase.api.token.domain.dto.request.TokenRequestDto;
import com.bootjpabase.api.token.domain.dto.response.TokenResponseDTO;
import com.bootjpabase.api.token.domain.entity.RefreshToken;
import com.bootjpabase.api.token.repository.TokenRepository;
import com.bootjpabase.api.user.domain.entity.User;
import com.bootjpabase.global.enums.user.TokenType;
import com.bootjpabase.global.security.jwt.component.TokenProvider;
import com.bootjpabase.global.enums.common.ApiReturnCode;
import com.bootjpabase.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TokenService {

    private final TokenRepository tokenRepository;
    private final TokenProvider tokenProvider;

    /**
     * 리프레쉬 토큰 정보 검증 후 엑세스 토큰 재발급
     *
     * @param dto 리프레쉬 토큰 dto
     * @return 토큰 dto
     */
    public TokenResponseDTO refreshAccessToken(TokenRequestDto dto) {

        String refreshToken = dto.getRefreshToken().replace("Bearer ", "");

        // refresh token 유효성 검사
        tokenProvider.validateToken(refreshToken, TokenType.REFRESH);

        // 저장된 토큰 조회
        RefreshToken savedToken = Optional.ofNullable(tokenRepository.findByToken(refreshToken))
                .orElseThrow(() -> new BusinessException(ApiReturnCode.UNSUPPORTED_TOKEN_ERROR));

        // 토큰 정보로 사용자 조회
        User user = savedToken.getUser();

        // access 토큰 재발급
        String accessToken = tokenProvider.createToken(user, TokenType.ACCESS);

        return TokenResponseDTO.builder()
                .accessToken(accessToken)
                .build();
    }
}
