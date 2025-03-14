package com.bootjpabase.api.user.service;

import com.bootjpabase.api.user.domain.dto.request.UserSaveRequestDTO;
import com.bootjpabase.api.user.domain.dto.request.UserUpdateRequestDTO;
import com.bootjpabase.api.user.domain.entity.User;
import com.bootjpabase.api.user.repository.UserRepository;
import com.bootjpabase.api.user.repository.UserRepositoryCustom;
import com.bootjpabase.global.enums.common.ApiReturnCode;
import com.bootjpabase.global.enums.file.UploadFileType;
import com.bootjpabase.global.exception.BusinessException;
import com.bootjpabase.global.file.domain.entity.File;
import com.bootjpabase.global.file.service.FileServiceTx;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceTx {

    private final UserRepository userRepository;
    private final UserRepositoryCustom userRepositoryCustom;
    private final BCryptPasswordEncoder encoder;
    private final FileServiceTx fileServiceTx;

    /**
     * 사용자 저장
     * @param dto
     * @return
     */
    public boolean saveUser(UserSaveRequestDTO dto, MultipartFile profileImgFile) throws IOException {

        // 저장할 entity 객체 생성
        User saveUser = createUserEntity(dto, profileImgFile);

        // 사용자 저장
        userRepository.save(saveUser);

        return true;
    }

    /**
     * user entity 생성 (저장시)
     * @param dto
     * @return
     */
    public User createUserEntity(UserSaveRequestDTO dto, MultipartFile profileImgFile) throws IOException {

        // validate
        validateUser(dto);

        // 프로필 이미지 파일 저장
        File saveProfileImgFile = fileServiceTx.saveFile(profileImgFile, UploadFileType.USER);

        return User.builder()
                .userId(dto.getUserId())
                .userName(dto.getUserName())
                .userPassword(encoder.encode(dto.getUserPassword()))
                .userPhone(dto.getUserPhone())
                .userEmail(dto.getUserEmail())
                .profileImgFileSn(saveProfileImgFile.getFileSn())
                .build();
    }

    /**
     * user validate (저장시)
     * @param dto
     */
    public void validateUser(UserSaveRequestDTO dto) {

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
    }

    /**
     * 사용자 수정
     * @param dto
     * @return
     */
    public boolean updateUser(UserUpdateRequestDTO dto, MultipartFile profileImgFile) throws IOException {

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
        User updateUser = userRepository.findById(dto.getUserSn())
                .orElseThrow(() -> new BusinessException(ApiReturnCode.NO_DATA_ERROR));

        // entity 영속성 컨텍스트 수정
        updateUser(updateUser, dto, profileImgFile);

        return true;
    }

    /**
     * user 수정 (수정할 값이 있는 데이타만 수정)
     * @param user
     * @param dto
     */
    private void updateUser(User user, UserUpdateRequestDTO dto, MultipartFile profileImgFile) throws IOException {

        Optional.ofNullable(dto.getUserPassword()).ifPresent(user::setUserPassword);
        Optional.ofNullable(dto.getUserPhone()).ifPresent(user::setUserPhone);
        Optional.ofNullable(dto.getUserEmail()).ifPresent(user::setUserEmail);

        if(!ObjectUtils.isEmpty(profileImgFile)) {
            File saveProfileImgFile = fileServiceTx.saveFile(profileImgFile, UploadFileType.USER);
            user.setProfileImgFileSn(saveProfileImgFile.getFileSn());
        }
    }

    /**
     * 사용자 삭제
     * @param userSn
     * @return
     */
    public boolean deleteUser(Long userSn) {

        // 삭제할 entity 조회
        User deleteUser = userRepository.findById(userSn)
                .orElseThrow(() -> new BusinessException(ApiReturnCode.NO_DATA_ERROR));

        // 사용자 프로필 이미지 파일 삭제
        if(deleteUser.getProfileImgFileSn() != null) {
            fileServiceTx.deleteFile(deleteUser.getProfileImgFileSn());
        }

        // 사용자 삭제
        userRepository.delete(deleteUser);

        return true;
    }
}
