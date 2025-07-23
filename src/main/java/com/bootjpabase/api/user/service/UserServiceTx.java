package com.bootjpabase.api.user.service;

import com.bootjpabase.api.file.domain.entity.File;
import com.bootjpabase.api.file.service.FileServiceTx;
import com.bootjpabase.api.token.domain.dto.response.TokenResponseDTO;
import com.bootjpabase.api.token.domain.entity.RefreshToken;
import com.bootjpabase.api.token.repository.TokenRepository;
import com.bootjpabase.api.token.service.TokenServiceTx;
import com.bootjpabase.api.user.domain.dto.request.UserLoginRequestDTO;
import com.bootjpabase.api.user.domain.dto.request.UserSaveRequestDTO;
import com.bootjpabase.api.user.domain.dto.request.UserUpdateRequestDTO;
import com.bootjpabase.api.user.domain.dto.response.UserResponseDTO;
import com.bootjpabase.api.user.domain.entity.User;
import com.bootjpabase.api.user.mapper.UserMapper;
import com.bootjpabase.api.user.repository.UserRepository;
import com.bootjpabase.global.enums.user.TokenType;
import com.bootjpabase.global.security.jwt.component.TokenProvider;
import com.bootjpabase.global.enums.common.ApiReturnCode;
import com.bootjpabase.global.enums.file.UploadFileType;
import com.bootjpabase.global.exception.BusinessException;
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

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final FileServiceTx fileServiceTx;
    private final TokenProvider tokenProvider;
    private final TokenRepository tokenRepository;
    private final TokenServiceTx tokenServiceTx;

    /**
     * User 저장
     *
     * @param dto            User 저장 요청 dto
     * @param profileImgFile 프로필 사진
     * @return 저장된 User 응답 dto
     * @throws IOException IOException 처리
     */
    public UserResponseDTO saveUser(UserSaveRequestDTO dto, MultipartFile profileImgFile) throws IOException {

        // 저장 전 data validate
        validateUser(dto);

        // password 암호화
        dto.setUserPassword(encoder.encode(dto.getUserPassword()));

        // 요청 dto를 entity로 변환
        User user = userMapper.toUserEntity(dto);

        // 프로필 이미지 파일이 있을 경우 저장
        if (!ObjectUtils.isEmpty(profileImgFile)) {
            File saveProfileImgFile = fileServiceTx.saveFile(profileImgFile, UploadFileType.USER);
            user.updateProfileImgFileSn(saveProfileImgFile.getFileSn());
        }

        // entity 저장 후 dto 반환
        return userMapper.toUserResponseDTO(userRepository.save(user));
    }

    /**
     * User validate
     *
     * @param dto User 저장 요청 dto
     */
    private void validateUser(UserSaveRequestDTO dto) {

        // userId 중복 체크
        if (userRepository.existsByUserId(dto.getUserId())) {
            throw new BusinessException(ApiReturnCode.ID_CONFLICT_ERROR);
        }

        // userPhone 중복 체크
        if (userRepository.existsByUserPhone(dto.getUserPhone())) {
            throw new BusinessException(ApiReturnCode.PHONE_CONFLICT_ERROR);
        }

        // userEmail 중복 체크
        if (userRepository.existsByUserEmail(dto.getUserEmail())) {
            throw new BusinessException(ApiReturnCode.EMAIL_CONFLICT_ERROR);
        }
    }

    /**
     * User 수정
     *
     * @param userSn         수정할 User 순번
     * @param dto            User 수정 요청 dto
     * @param profileImgFile 프로필 사진
     * @return 수정된 User 응답 dto
     * @throws IOException IOException 처리
     */
    public UserResponseDTO updateUser(Long userSn, UserUpdateRequestDTO dto, MultipartFile profileImgFile) throws IOException {

        // 수정할 entity 조회
        User user = userRepository.findById(userSn)
                .orElseThrow(() -> new BusinessException(ApiReturnCode.NO_DATA_ERROR));

        // 비밀번호 인코딩
        String encodedPassword = null;
        if (!ObjectUtils.isEmpty(dto.getUserPassword())) {
            encodedPassword = encoder.encode(dto.getUserPassword());
        }

        // 프로필 사진
        Long profileImgFileSn = null;
        if (!ObjectUtils.isEmpty(profileImgFile)) {
            File savedProfileImgFile = fileServiceTx.saveFile(profileImgFile, UploadFileType.USER);
            profileImgFileSn = savedProfileImgFile.getFileSn();
        }

        // entity 영속성 컨텍스트 수정
        user.updateUserInfo(dto, encodedPassword, profileImgFileSn);

        return userMapper.toUserResponseDTO(user);
    }

    /**
     * User 삭제
     *
     * @param userSn 삭제할 User 순번
     * @return 삭제된 User 응답 dto
     */
    public UserResponseDTO deleteUser(Long userSn) {

        // 먼저 RefreshToken을 삭제
        RefreshToken refreshToken = tokenRepository.findByUser_UserSn(userSn);
        if (refreshToken != null) {
            tokenRepository.delete(refreshToken);
        }

        // 삭제할 entity 조회
        User deleteUser = userRepository.findById(userSn)
                .orElseThrow(() -> new BusinessException(ApiReturnCode.NO_DATA_ERROR));

        // 사용자 프로필 이미지 파일 삭제
        if (deleteUser.getProfileImgFileSn() != null) {
            fileServiceTx.deleteFile(deleteUser.getProfileImgFileSn());
        }

        // 사용자 삭제
        userRepository.delete(deleteUser);

        // 삭제 후 dto 반환
        return userMapper.toUserResponseDTO(deleteUser);
    }

    /**
     * 사용자 로그인
     *
     * @param dto 로그인 요청 dto
     * @return token 응답 dto
     */
    public TokenResponseDTO userLogin(UserLoginRequestDTO dto) {
        TokenResponseDTO tokenDto;

        // 해당 계정 조회
        User user = userRepository.findByUserId(dto.getUserId())
                .orElseThrow(() -> new BusinessException(ApiReturnCode.LOGIN_ID_FAIL_ERROR));

        // 비밀번호 체크
        if (!encoder.matches(dto.getUserPassword(), user.getUserPassword())) {
            throw new BusinessException(ApiReturnCode.LOGIN_PWD_FAIL_ERROR);
        }

        // access and refresh token 발급
        tokenDto = tokenProvider.createAllToken(user);

        // DB에 저장된 refresh token 조회
        RefreshToken savedToken = tokenRepository.findByUser(user);

        // 있으면 token 업데이트, 없으면 새로 저장
        if (!org.apache.commons.lang3.ObjectUtils.isEmpty(savedToken)) {
            savedToken.updateToken(tokenDto.getRefreshToken());
        } else {
            tokenServiceTx.saveRefreshToken(tokenDto.getRefreshToken(), user);
        }

        return tokenDto;
    }

    /**
     * 로그아웃 (refresh 토큰 삭제)
     *
     * @param token 토큰
     * @return 삭제 결과
     */
    public boolean logoutUser(String token) {

        String accessToken = token.replace("Bearer ", "");

        // access token 유효성 검사
        tokenProvider.validateToken(accessToken, TokenType.ACCESS);

        // access 토큰에서 사용자 아이디 추출
        String id = tokenProvider.getIdFromToken(accessToken);

        // 아이디로 사용자 entity 조회
        User user = userRepository.findByUserId(id)
                .orElseThrow(() -> new BusinessException(ApiReturnCode.NO_DATA_ERROR));

        // 삭제할 refresh 토큰 entity 조회
        RefreshToken savedToken = Optional.ofNullable(tokenRepository.findByUser(user))
                .orElseThrow(() -> new BusinessException(ApiReturnCode.UNSUPPORTED_TOKEN_ERROR));

        // refresh 토큰 삭제
        tokenRepository.delete(savedToken);

        return true;
    }
}
