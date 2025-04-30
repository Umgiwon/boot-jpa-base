package com.bootjpabase.api.user.controller;

import com.bootjpabase.api.user.domain.dto.request.UserListRequestDTO;
import com.bootjpabase.api.user.domain.dto.request.UserLoginRequestDTO;
import com.bootjpabase.api.user.domain.dto.request.UserSaveRequestDTO;
import com.bootjpabase.api.user.domain.dto.request.UserUpdateRequestDTO;
import com.bootjpabase.api.user.domain.dto.response.UserResponseDTO;
import com.bootjpabase.api.user.service.UserService;
import com.bootjpabase.api.user.service.UserServiceTx;
import com.bootjpabase.global.config.jwt.domain.dto.TokenResponseDTO;
import com.bootjpabase.global.constant.ResponseMessageConst;
import com.bootjpabase.global.domain.dto.BaseResponse;
import com.bootjpabase.global.domain.dto.Pagination;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "User API", description = "사용자 API")
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final UserServiceTx userServiceTx;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "저장 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "204", description = "저장 실패", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @Operation(summary = "사용자 저장", description = "사용자 저장 API")
    @PostMapping(value = "", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public BaseResponse saveUser(
            @Valid @RequestPart UserSaveRequestDTO dto,
            @RequestPart(required = false, name = "profileImgFile") MultipartFile profileImgFile
    ) throws Exception {
        BaseResponse baseResponse;

        // 사용자 저장
        boolean result = userServiceTx.saveUser(dto, profileImgFile);

        // response set
        baseResponse = result
                ? BaseResponse.getBaseResponseBuilder(HttpStatus.OK.value(), ResponseMessageConst.SAVE_SUCCESS, 1 , true)
                : BaseResponse.getBaseResponseBuilder(HttpStatus.BAD_REQUEST.value(), ResponseMessageConst.SAVE_FAIL, 0, false);

        return baseResponse;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "204", description = "내용 없음", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @Operation(summary = "사용자 목록 조회", description = "사용자 목록 조회 API")
    @GetMapping("")
    public BaseResponse getUserList(
            @ParameterObject UserListRequestDTO dto,
            @PageableDefault(page = 0, size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable
    ) throws Exception {
        BaseResponse baseResponse;

        // 사용자 목록 조회
        Page<UserResponseDTO> resultPaging = userService.getUserList(dto, pageable);
        List<UserResponseDTO> resultList = resultPaging.getContent();

        // response set
        baseResponse = !ObjectUtils.isEmpty(resultList)
                ? BaseResponse.getBaseResponseBuilder(HttpStatus.OK.value(), ResponseMessageConst.SELECT_SUCCESS, resultList.size(), resultList, new Pagination(resultPaging))
                : BaseResponse.getBaseResponseBuilder(HttpStatus.NO_CONTENT.value(), ResponseMessageConst.NO_CONTENT, 0, new ArrayList<>());

        return baseResponse;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "204", description = "수정 실패", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @Operation(summary = "사용자 수정", description = "사용자 수정 API")
    @PatchMapping("/{userSn}")
    public BaseResponse updateUser(
            @PathVariable("userSn") Long userSn,
            @Valid @RequestPart UserUpdateRequestDTO dto,
            @RequestPart(required = false, name = "profileImgFile") MultipartFile profileImgFile
    ) throws Exception {
        BaseResponse baseResponse;

        // 사용자 수정
        boolean result = userServiceTx.updateUser(userSn, dto, profileImgFile);

        // response set
        baseResponse = result
                ? BaseResponse.getBaseResponseBuilder(HttpStatus.OK.value(), ResponseMessageConst.SAVE_SUCCESS, 1 , true)
                : BaseResponse.getBaseResponseBuilder(HttpStatus.BAD_REQUEST.value(), ResponseMessageConst.SAVE_FAIL, 0, false);

        return baseResponse;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "204", description = "삭제 실패", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @Operation(summary = "사용자 삭제", description = "사용자 삭제 API")
    @DeleteMapping("/{userSn}")
    public BaseResponse deleteUser(
            @PathVariable("userSn") Long userSn
    ) throws Exception {
        BaseResponse baseResponse;

        // 사용자 삭제
        boolean result = userServiceTx.deleteUser(userSn);

        baseResponse = result
                ? BaseResponse.getBaseResponseBuilder(HttpStatus.OK.value(), ResponseMessageConst.DELETE_SUCCESS, 1, true)
                : BaseResponse.getBaseResponseBuilder(HttpStatus.BAD_REQUEST.value(), ResponseMessageConst.DELETE_FAIL, 0, false);

        return baseResponse;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = TokenResponseDTO.class))),
            @ApiResponse(responseCode = "204", description = "내용 없음", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @Operation(summary = "사용자 로그인", description = "사용자 로그인 API")
    @PostMapping("/login")
    public BaseResponse userLogin(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "json",
                    content = @Content(schema = @Schema(implementation = UserLoginRequestDTO.class)))
            @RequestBody @Valid UserLoginRequestDTO dto
    ) throws Exception {
        BaseResponse baseResponse;

        TokenResponseDTO result = userServiceTx.userLogin(dto);

        baseResponse = !ObjectUtils.isEmpty(result)
                ? BaseResponse.getBaseResponseBuilder(HttpStatus.OK.value(), ResponseMessageConst.LOGIN_SUCCESS, 1, result)
                : BaseResponse.getBaseResponseBuilder(HttpStatus.NO_CONTENT.value(), ResponseMessageConst.LOGIN_FAIL, 0, TokenResponseDTO.builder().build());

        return baseResponse;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "204", description = "실패", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @Operation(summary = "로그아웃", description = "로그아웃 API - refresh token 삭제")
    @PostMapping("logout")
    public BaseResponse logoutManager(@RequestHeader(value = "Authorization", required = false) String token) throws Exception {
        BaseResponse baseResponse;

        // 관리자 로그아웃
        boolean result = userServiceTx.logoutManager(token);

        // response set
        baseResponse = result
                ? BaseResponse.getBaseResponseBuilder(HttpStatus.OK.value(), ResponseMessageConst.LOGOUT_SUCCESS, 1 , true)
                : BaseResponse.getBaseResponseBuilder(HttpStatus.BAD_REQUEST.value(), ResponseMessageConst.LOGOUT_FAIL, 0, false);

        return baseResponse;
    }
}
