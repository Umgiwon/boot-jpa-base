package com.bootjpabase.api.manager.controller;

import com.nangman.api.admin.system.manager.domain.dto.request.*;
import com.nangman.api.admin.system.manager.domain.dto.response.ManagerDetailResponseDTO;
import com.nangman.api.admin.system.manager.domain.dto.response.ManagerResponseDTO;
import com.nangman.api.admin.system.manager.service.ManagerService;
import com.nangman.api.admin.system.manager.service.ManagerServiceTx;
import com.nangman.global.config.jwt.domain.dto.TokenResponseDTO;
import com.nangman.global.constant.ResponseMessageConst;
import com.nangman.global.domain.dto.BaseResponse;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Manager API", description = "관리자 API")
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/admin/system/manager/")
public class ManagerController {

    private final ManagerService managerService;
    private final ManagerServiceTx managerServiceTx;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "저장 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "204", description = "저장 실패", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @Operation(summary = "관리자 저장", description = "관리자 저장 API")
    @PostMapping("save")
    public BaseResponse saveManager(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "json",
                    content = @Content(schema = @Schema(implementation = ManagerSaveRequestDTO.class)))
            @RequestBody @Valid ManagerSaveRequestDTO dto
    ) throws Exception {
        BaseResponse baseResponse;

        // 관리자 저장
        boolean result = managerServiceTx.saveManager(dto);

        // response set
        baseResponse = result
                ? BaseResponse.getBaseResponseBuilder(HttpStatus.OK.value(), ResponseMessageConst.SAVE_SUCCESS, 1 , true)
                : BaseResponse.getBaseResponseBuilder(HttpStatus.NO_CONTENT.value(), ResponseMessageConst.SAVE_FAIL, 0, false);

        return baseResponse;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ManagerResponseDTO.class))),
            @ApiResponse(responseCode = "204", description = "내용 없음", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @Operation(summary = "관리자 목록 조회", description = "관리자 목록 조회 API")
    @GetMapping("list")
    public BaseResponse getManagerList(
            @Parameter(name = "name", description = "이름", example = "고수일", in = ParameterIn.QUERY, schema = @Schema(implementation = String.class))
            @RequestParam(required = false) String name,
            @PageableDefault(page = 0, size = 10, sort = "regDt", direction = Sort.Direction.DESC) Pageable pageable
    ) throws Exception {
        BaseResponse baseResponse;

        // 조회용 dto set
        ManagerListRequestDTO dto = ManagerListRequestDTO.builder()
                .name(ObjectUtils.isEmpty(name) ? null : name)
                .build();

        // 관리자 목록 조회
        List<ManagerResponseDTO> resultList = managerService.getManagerList(dto, pageable);

        // response set
        baseResponse = !ObjectUtils.isEmpty(resultList)
                ? BaseResponse.getBaseResponseBuilder(HttpStatus.OK.value(), ResponseMessageConst.SELECT_SUCCESS, resultList.size(), resultList)
                : BaseResponse.getBaseResponseBuilder(HttpStatus.NO_CONTENT.value(), ResponseMessageConst.NO_CONTENT, 0, ManagerResponseDTO.builder().build());

        return baseResponse;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ManagerDetailResponseDTO.class))),
            @ApiResponse(responseCode = "204", description = "내용 없음", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @Operation(summary = "관리자 상세 조회", description = "관리자 상세 조회 API")
    @GetMapping("")
    public BaseResponse getManager(
            @Parameter(name = "id", description = "아이디", example = "admin", in = ParameterIn.QUERY, schema = @Schema(implementation = String.class))
            @RequestParam(required = false) String id
    ) throws Exception {
        BaseResponse baseResponse;

        // 관리자 상세 조회
        ManagerDetailResponseDTO result = managerService.getManager(id);

        // response set
        baseResponse = !ObjectUtils.isEmpty(result)
                ? BaseResponse.getBaseResponseBuilder(HttpStatus.OK.value(), ResponseMessageConst.SELECT_SUCCESS, 1, result)
                : BaseResponse.getBaseResponseBuilder(HttpStatus.NO_CONTENT.value(), ResponseMessageConst.NO_CONTENT, 0, ManagerDetailResponseDTO.builder().build());

        return baseResponse;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "204", description = "수정 실패", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @Operation(summary = "관리자 수정", description = "관리자 수정 API")
    @PatchMapping("update")
    public BaseResponse updateManager(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "json",
                    content = @Content(schema = @Schema(implementation = ManagerUpdateRequestDTO.class)))
            @RequestBody @Valid ManagerUpdateRequestDTO dto
    ) throws Exception {
        BaseResponse baseResponse;

        // 관리자 수정
        boolean result = managerServiceTx.updateManager(dto);

        // response set
        baseResponse = result
                ? BaseResponse.getBaseResponseBuilder(HttpStatus.OK.value(), ResponseMessageConst.SAVE_SUCCESS, 1 , true)
                : BaseResponse.getBaseResponseBuilder(HttpStatus.NO_CONTENT.value(), ResponseMessageConst.SAVE_FAIL, 0, false);

        return baseResponse;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "204", description = "삭제 실패", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @Operation(summary = "관리자 삭제", description = "관리자 삭제 API")
    @DeleteMapping("delete")
    public BaseResponse deleteManager(
            @Parameter(name = "id", description = "아이디", example = "admin", in = ParameterIn.QUERY, schema = @Schema(implementation = String.class))
            @RequestParam String id
    ) throws Exception {
        BaseResponse baseResponse;

        // 관리자 삭제
        boolean result = managerServiceTx.deleteManager(id);

        baseResponse = result
                ? BaseResponse.getBaseResponseBuilder(HttpStatus.OK.value(), ResponseMessageConst.DELETE_SUCCESS, 1, true)
                : BaseResponse.getBaseResponseBuilder(HttpStatus.NO_CONTENT.value(), ResponseMessageConst.DELETE_FAIL, 0, false);

        return baseResponse;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = TokenResponseDTO.class))),
            @ApiResponse(responseCode = "204", description = "내용 없음", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @Operation(summary = "관리자 로그인", description = "관리자 로그인 API")
    @GetMapping("login")
    public BaseResponse managerLogin(
            @Parameter(name = "id", description = "아이디", example = "admin", in = ParameterIn.QUERY, schema = @Schema(implementation = String.class))
            @RequestParam String id,
            @Parameter(name = "password", description = "비밀번호", example = "admin!", in = ParameterIn.QUERY, schema = @Schema(implementation = String.class))
            @RequestParam String password
    ) throws Exception {
        BaseResponse baseResponse;

        // 로그인용 dto set
        ManagerLoginRequestDTO dto = ManagerLoginRequestDTO.builder()
                .id(id)
                .password(password)
                .build();

        TokenResponseDTO result = managerService.managerLogin(dto);

        baseResponse = !ObjectUtils.isEmpty(result)
                ? BaseResponse.getBaseResponseBuilder(HttpStatus.OK.value(), ResponseMessageConst.LOGIN_SUCCESS, 1, result)
                : BaseResponse.getBaseResponseBuilder(HttpStatus.NO_CONTENT.value(), ResponseMessageConst.LOGIN_FAIL, 0, TokenResponseDTO.builder().build());

        return baseResponse;
    }

    // TODO 로그아웃 구현

}
