package com.bootjpabase.api.user.controller;

import com.bootjpabase.api.token.domain.dto.response.TokenResponseDTO;
import com.bootjpabase.api.user.domain.dto.request.UserListRequestDTO;
import com.bootjpabase.api.user.domain.dto.request.UserLoginRequestDTO;
import com.bootjpabase.api.user.domain.dto.request.UserSaveRequestDTO;
import com.bootjpabase.api.user.domain.dto.request.UserUpdateRequestDTO;
import com.bootjpabase.api.user.domain.dto.response.UserResponseDTO;
import com.bootjpabase.api.user.service.UserService;
import com.bootjpabase.api.user.service.UserServiceTx;
import com.bootjpabase.global.annotation.common.CustomApiLogger;
import com.bootjpabase.global.constant.ResponseMessageConst;
import com.bootjpabase.global.domain.dto.BaseResponse;
import com.bootjpabase.global.domain.dto.BaseResponseFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "User API", description = "사용자 API")
@CustomApiLogger
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;     // 조회 전용
    private final UserServiceTx userServiceTx; // 등록, 수정, 삭제 전용

    @Operation(summary = "사용자 저장", description = "사용자 저장 (프로필 이미지 첨부파일 가능)")
    @PostMapping(value = "", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public BaseResponse<UserResponseDTO> saveUser(
            @RequestPart @Valid UserSaveRequestDTO dto,
            @RequestPart(required = false, name = "profileImgFile") MultipartFile profileImgFile
    ) throws Exception {
        return BaseResponseFactory.success(userServiceTx.saveUser(dto, profileImgFile));
    }

    @Operation(summary = "사용자 목록 조회", description = "이름으로 목록 조회 (페이징 처리)")
    @GetMapping("")
    public BaseResponse<List<UserResponseDTO>> getUserList(
            @ParameterObject UserListRequestDTO dto,
            @Parameter(
                    description = "페이징 정보 (가능한 정렬조건 : userSn, userId, userName, userPhone, userEmail, createdDate)",
                    example = """
                            {
                              "page": 0,
                              "size": 10,
                              "sort": ["createdDate,desc"]
                            }
                            """
            )
            @PageableDefault Pageable pageable
    ) {
        return BaseResponseFactory.success(userService.getUserList(dto, pageable));
    }

    @Operation(summary = "사용자 수정", description = "비밀번호, 전화번호, 이메일, 프로필 이미지 첨부파일 수정 가능")
    @PatchMapping(value = "/{userSn}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public BaseResponse<UserResponseDTO> updateUser(
            @PathVariable("userSn") Long userSn,
            @Valid @RequestPart UserUpdateRequestDTO dto,
            @RequestPart(required = false, name = "profileImgFile") MultipartFile profileImgFile
    ) throws Exception {
        return BaseResponseFactory.success(userServiceTx.updateUser(userSn, dto, profileImgFile));
    }

    @Operation(summary = "사용자 삭제", description = "사용자 순번으로 삭제")
    @DeleteMapping("/{userSn}")
    public BaseResponse<UserResponseDTO> deleteUser(
            @PathVariable("userSn") Long userSn
    ) {
        return BaseResponseFactory.success(userServiceTx.deleteUser(userSn));
    }

    @Operation(summary = "사용자 로그인", description = "사용자 로그인 API")
    @PostMapping("/login")
    public BaseResponse<TokenResponseDTO> loginUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "json",
                    content = @Content(schema = @Schema(implementation = UserLoginRequestDTO.class)))
            @RequestBody @Valid UserLoginRequestDTO dto
    ) {
        return BaseResponseFactory.successWithMessage(
                userServiceTx.userLogin(dto)
                , ResponseMessageConst.LOGIN_SUCCESS
        );
    }

    @Operation(summary = "로그아웃", description = "로그아웃 API - refresh token 삭제")
    @PostMapping("/logout")
    public BaseResponse<Boolean> logoutUser(
            @RequestHeader(value = "Authorization", required = false) String token
    ) {
        return BaseResponseFactory.successWithMessage(
                userServiceTx.logoutUser(token)
                , ResponseMessageConst.LOGOUT_SUCCESS
        );
    }
}
