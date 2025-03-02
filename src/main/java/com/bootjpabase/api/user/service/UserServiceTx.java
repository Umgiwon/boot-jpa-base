package com.bootjpabase.api.user.service;

import com.bootjpabase.api.user.domain.dto.request.UserSaveRequestDTO;
import com.bootjpabase.api.user.domain.dto.request.UserUpdateRequestDTO;
import com.bootjpabase.api.user.domain.entity.User;
import com.bootjpabase.api.user.repository.UserRepository;
import com.bootjpabase.api.user.repository.UserRepositoryCustom;
import com.bootjpabase.global.enums.common.ApiReturnCode;
import com.bootjpabase.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceTx {

    private final UserRepository userRepository;
    private final UserRepositoryCustom userRepositoryCustom;
    private final BCryptPasswordEncoder encoder;

    /**
     * 사용자 저장
     * @param dto
     * @return
     */
    public boolean saveUser(UserSaveRequestDTO dto) {
        boolean result = false;

        // userId 중복 체크
        if(userRepository.existsByUserId(dto.getUserId())) {
            throw new BusinessException(ApiReturnCode.ID_CONFLICT_ERROR);
        }

        // userPhone 중복 체크
        if(userRepository.existsByUserPhone(dto.getUserPhone())) {
            throw new BusinessException(ApiReturnCode.PHONE_CONFLICT_ERROR);
        }

        // userEmail 중복 체크
        if(userRepository.existsByUserEmail(dto.getUserEmail())) {
            throw new BusinessException(ApiReturnCode.EMAIL_CONFLICT_ERROR);
        }

        // 저장할 entity 객체 생성
        User saveUser = User.builder()
                .userId(dto.getUserId())
                .userName(dto.getUserName())
                .userPassword(encoder.encode(dto.getUserPassword()))
                .userPhone(dto.getUserPhone())
                .userEmail(dto.getUserEmail())
                .build();

        // 사용자 저장
        userRepository.save(saveUser);
        result = true;

        return result;
    }

    /**
     * 사용자 수정
     * @param dto
     * @return
     */
    public boolean updateUser(UserUpdateRequestDTO dto) {
        boolean result = false;

//        // userPhone 중복 체크
//        if(userRepository.existsByUserPhone(dto.getUserPhone())) {
//            throw new BusinessException(ApiReturnCode.PHONE_CONFLICT_ERROR);
//        }
//
//        // userEmail 중복 체크
//        if(userRepository.existsByUserEmail(dto.getUserEmail())) {
//            throw new BusinessException(ApiReturnCode.EMAIL_CONFLICT_ERROR);
//        }

        // 수정할 entity 조회
        User updateUser = userRepository.findById(dto.getUserSn()).orElse(null);

        if(!ObjectUtils.isEmpty(updateUser)) {

            // entity 영속성 컨텍스트 수정
            if(!ObjectUtils.isEmpty(encoder.encode(dto.getUserPassword()))) {
                updateUser.setUserPassword(dto.getUserPassword());
            }

            if(!ObjectUtils.isEmpty(dto.getUserPhone())) {
                updateUser.setUserPhone(dto.getUserPhone());
            }

            if(!ObjectUtils.isEmpty(dto.getUserEmail())) {
                updateUser.setUserEmail(dto.getUserEmail());
            }

            result = true;
        }

        return result;
    }

    /**
     * 사용자 삭제
     * @param userSn
     * @return
     */
    public boolean deleteUser(Long userSn) {
        boolean result = false;

        // 삭제할 entity 조회
        User deleteUser = userRepository.findById(userSn).orElse(null);

        // 삭제
        if(!ObjectUtils.isEmpty(deleteUser)) {
            userRepository.delete(deleteUser);
            result = true;
        }

        return result;
    }
}
