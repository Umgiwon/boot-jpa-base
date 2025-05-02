package com.bootjpabase.api.user.service;

import com.bootjpabase.api.user.domain.dto.request.UserLoginRequestDTO;
import com.bootjpabase.api.user.domain.dto.request.UserSaveRequestDTO;
import com.bootjpabase.api.user.domain.dto.request.UserUpdateRequestDTO;
import com.bootjpabase.api.user.domain.dto.response.UserResponseDTO;
import com.bootjpabase.api.user.domain.entity.User;
import com.bootjpabase.api.user.repository.UserRepository;
import com.bootjpabase.api.user.repository.UserRepositoryCustom;
import com.bootjpabase.global.config.jwt.component.TokenProvider;
import com.bootjpabase.global.config.jwt.domain.dto.TokenResponseDTO;
import com.bootjpabase.global.config.jwt.domain.entity.RefreshToken;
import com.bootjpabase.global.config.jwt.repository.TokenRepository;
import com.bootjpabase.global.config.jwt.service.TokenServiceTx;
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
    private final TokenProvider tokenProvider;
    private final TokenRepository tokenRepository;
    private final TokenServiceTx tokenServiceTx;

    /**
     * 사용자 저장
     * @param dto
     * @return
     */
    public UserResponseDTO saveUser(UserSaveRequestDTO dto, MultipartFile profileImgFile) throws IOException {

        // 저장할 entity 객체 생성
        User saveUser = createUserEntity(dto);

        // 프로필 이미지 파일이 있을 경우 저장
        if(!ObjectUtils.isEmpty(profileImgFile)) {
            File saveProfileImgFile = fileServiceTx.saveFile(profileImgFile, UploadFileType.USER);
            saveUser.setProfileImgFileSn(saveProfileImgFile.getFileSn());
        }

        // 사용자 저장 후 dto 반환
        return userEntityToDto(userRepository.save(saveUser));
    }

    /**
     * user entity 생성 (저장시)
     * @param dto
     * @return
     */
    public User createUserEntity(UserSaveRequestDTO dto) throws IOException {

        // validate
        validateUser(dto);

        return User.builder()
                .userId(dto.getUserId())
                .userName(dto.getUserName())
                .userPassword(encoder.encode(dto.getUserPassword()))
                .userPhone(dto.getUserPhone())
                .userEmail(dto.getUserEmail())
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
    public UserResponseDTO updateUser(Long userSn, UserUpdateRequestDTO dto, MultipartFile profileImgFile) throws IOException {

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
        User updateUser = userRepository.findById(userSn)
                .orElseThrow(() -> new BusinessException(ApiReturnCode.NO_DATA_ERROR));

        // entity 영속성 컨텍스트 수정
        updateUser(updateUser, dto, profileImgFile);

        return userEntityToDto(updateUser);
    }

    /**
     * user 수정 (수정할 값이 있는 데이타만 수정)
     * @param user
     * @param dto
     */
    private void updateUser(User user, UserUpdateRequestDTO dto, MultipartFile profileImgFile) throws IOException {

        // 비밃번호
        if(!ObjectUtils.isEmpty(dto.getUserPassword())) {
            user.setUserPassword(encoder.encode(dto.getUserPassword()));
        }

        Optional.ofNullable(dto.getUserPhone()).ifPresent(user::setUserPhone); // 전화번호
        Optional.ofNullable(dto.getUserEmail()).ifPresent(user::setUserEmail); // 이메일

        // 프로필 사진
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
    public UserResponseDTO deleteUser(Long userSn) {

        // 삭제할 entity 조회
        User deleteUser = userRepository.findById(userSn)
                .orElseThrow(() -> new BusinessException(ApiReturnCode.NO_DATA_ERROR));

        // 사용자 프로필 이미지 파일 삭제
        if(deleteUser.getProfileImgFileSn() != null) {
            fileServiceTx.deleteFile(deleteUser.getProfileImgFileSn());
        }

        // 사용자 삭제
        userRepository.delete(deleteUser);

        return userEntityToDto(deleteUser);
    }

    /**
     * 사용자 로그인
     * @param dto
     * @return
     */
    public TokenResponseDTO userLogin(UserLoginRequestDTO dto) {
        TokenResponseDTO tokenDto;

        // 해당 계정 조회
        User user = userRepository.findByUserId(dto.getUserId())
                .orElseThrow(() -> new BusinessException(ApiReturnCode.LOGIN_ID_FAIL_ERROR));

        // 비밀번호 체크
        if(!encoder.matches(dto.getUserPassword(), user.getUserPassword())) {
            throw new BusinessException(ApiReturnCode.LOGIN_PWD_FAIL_ERROR);
        }

        // access & refresh token 발급
        tokenDto = tokenProvider.createAllToken(user);

        // DB에 저장된 refresh token 조회
        RefreshToken savedToken = tokenRepository.findByUser(user);

        // 있으면 token 업데이트, 없으면 새로 저장
        if(!org.apache.commons.lang3.ObjectUtils.isEmpty(savedToken)) {
            savedToken.setToken(tokenDto.getRefreshToken());
        } else {
            tokenServiceTx.saveRefreshToken(tokenDto.getRefreshToken(), user);
        }

        return tokenDto;
    }

    /**
     * 로그아웃 (refresh 토큰 삭제)
     * @param token
     * @return
     */
    public boolean logoutManager(String token) {

        String accessToken = token.replace("Bearer ", "");

        // access token 유효성 검사
        if(!tokenProvider.validateToken(accessToken)) {
            throw new BusinessException(ApiReturnCode.UNAUTHORIZED_TOKEN_ERROR);
        }

        // access 토큰에서 사용자 아이디 추출
        String id = tokenProvider.getIdFromToken(accessToken);

        // 아이디로 사용자 entity 조회
        User user = userRepository.findByUserId(id)
                .orElseThrow(() -> new BusinessException(ApiReturnCode.NO_DATA_ERROR));

        // 삭제할 refresh 토큰 entity 조회
        RefreshToken savedToken = Optional.ofNullable(tokenRepository.findByUser(user))
                .orElseThrow(() -> new BusinessException(ApiReturnCode.UNAUTHORIZED_TOKEN_ERROR));

        // refresh 토큰 삭제
        tokenRepository.delete(savedToken);

        return true;
    }

    /**
     * 사용자 entity를 dto로 변환
     * @param user
     * @return
     */
    private UserResponseDTO userEntityToDto(User user) {
        return UserResponseDTO.builder()
                .userSn(user.getUserSn())
                .userId(user.getUserId())
                .userName(user.getUserName())
                .userPhone(user.getUserPhone())
                .userEmail(user.getUserEmail())
                .build();
    }
}
