package com.bootjpabase.api.user.service;

import com.bootjpabase.api.user.domain.dto.request.UserListRequestDTO;
import com.bootjpabase.api.user.domain.dto.request.UserLoginRequestDTO;
import com.bootjpabase.api.user.domain.dto.response.UserResponseDTO;
import com.bootjpabase.api.user.domain.entity.User;
import com.bootjpabase.api.user.repository.UserRepository;
import com.bootjpabase.api.user.repository.UserRepositoryCustom;
import com.bootjpabase.global.config.jwt.component.TokenProvider;
import com.bootjpabase.global.config.jwt.domain.dto.TokenResponseDTO;
import com.bootjpabase.global.enums.common.ApiReturnCode;
import com.bootjpabase.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final UserRepositoryCustom userRepositoryCustom;
    private final BCryptPasswordEncoder encoder;
    private final TokenProvider tokenProvider;

    /**
     * 사용자 목록 조회
     * @param dto
     * @param pageable
     * @return
     */
    public Page<UserResponseDTO> getUserList(UserListRequestDTO dto, Pageable pageable) {
        return userRepositoryCustom.getUserList(dto, pageable);
    }

    /**
     * 사용자 로그인
     * @param dto
     * @return
     */
    public TokenResponseDTO userLogin(UserLoginRequestDTO dto) {
        TokenResponseDTO tokenDto;

        // 해당 계정 조회
        User user = userRepository.findByUserId(dto.getUserId()).orElse(null);

        // 해당 아이디 없을 경우
        if(ObjectUtils.isEmpty(user)) {
            throw new BusinessException(ApiReturnCode.LOGIN_ID_FAIL_ERROR);
        }

        // 비밀번호 체크
        if(!encoder.matches(dto.getUserPassword(), user.getUserPassword())) {
            throw new BusinessException(ApiReturnCode.LOGIN_PWD_FAIL_ERROR);
        }

        tokenDto = TokenResponseDTO.builder()
                .token(tokenProvider.createToken(user))
                .build();

        return tokenDto;
    }
}
