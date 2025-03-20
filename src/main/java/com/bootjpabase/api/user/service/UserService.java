package com.bootjpabase.api.user.service;

import com.bootjpabase.api.user.domain.dto.request.UserListRequestDTO;
import com.bootjpabase.api.user.domain.dto.response.UserResponseDTO;
import com.bootjpabase.api.user.repository.UserRepository;
import com.bootjpabase.api.user.repository.UserRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final UserRepositoryCustom userRepositoryCustom;

    /**
     * 사용자 목록 조회
     * @param dto
     * @param pageable
     * @return
     */
    public Page<UserResponseDTO> getUserList(UserListRequestDTO dto, Pageable pageable) {
        return userRepositoryCustom.getUserList(dto, pageable);
    }
}
