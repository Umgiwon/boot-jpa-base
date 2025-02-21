package com.bootjpabase.carmanager.api.sample.controller;

import com.bootjpabase.carmanager.api.sample.domain.dto.request.*;
import com.bootjpabase.carmanager.api.sample.domain.dto.response.SampleResponseDTO;
import com.bootjpabase.carmanager.api.sample.service.SampleService;
import com.bootjpabase.carmanager.api.sample.service.SampleServiceTx;
import com.bootjpabase.carmanager.global.annotation.sample.ValidContent;
import com.bootjpabase.carmanager.global.annotation.sample.ValidTitle;
import com.bootjpabase.carmanager.global.constant.ResponseMessageConst;
import com.bootjpabase.carmanager.global.domain.dto.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Sample API", description = "Sample API 설명")
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/sample/")
public class SampleController {

    private final SampleService sampleService;
    private final SampleServiceTx sampleServiceTx;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "저장 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "204", description = "저장 실패", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @Operation(summary = "샘플 단건 저장", description = "샘플 단건 저장 API")
    @PostMapping("")
    public BaseResponse saveSample(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "json",
                    content = @Content(schema = @Schema(implementation = SampleSaveRequestDTO.class)))
            @RequestBody @Valid SampleSaveRequestDTO dto
    ) throws Exception {
        BaseResponse baseResponse;

        // Sample 단건 저장
        boolean result = sampleServiceTx.saveSample(dto);

        // response set
        baseResponse = result
                ? BaseResponse.getBaseResponseBuilder(HttpStatus.OK.value(), ResponseMessageConst.SAVE_SUCCESS, 1 , true)
                : BaseResponse.getBaseResponseBuilder(HttpStatus.NO_CONTENT.value(), ResponseMessageConst.SAVE_FAIL, 0, false);

        return baseResponse;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "저장 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "204", description = "저장 실패", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @Operation(summary = "샘플 다건 저장", description = "샘플 다건 저장 API")
    @PostMapping("list")
    public BaseResponse saveSampleList(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "json", content = @Content(
                    examples = {
                            @ExampleObject(name = "예제1", value = """
                                            [
                                                {
                                                    "title": "title11",
                                                    "content": "content11"
                                                },
                                                {
                                                    "title": "title12",
                                                    "content": "content12"
                                                }
                                            ]
                                    """)
                    }
            ))
            @RequestBody @Valid List<SampleSaveRequestDTO> dto
    ) throws Exception {
        BaseResponse baseResponse;

        // Sample 다건 저장
        boolean result = sampleServiceTx.saveAllSample(dto);

        // response set
        baseResponse = result
                ? BaseResponse.getBaseResponseBuilder(HttpStatus.OK.value(), ResponseMessageConst.SAVE_SUCCESS, dto.size(), true)
                : BaseResponse.getBaseResponseBuilder(HttpStatus.NO_CONTENT.value(), ResponseMessageConst.SAVE_FAIL, 0, false);

        return baseResponse;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = SampleResponseDTO.class))),
            @ApiResponse(responseCode = "204", description = "내용 없음", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @Operation(summary = "샘플 단건 조회", description = "샘플 단건 조회 API")
    @GetMapping("")
    public BaseResponse getSample(
            @Parameter(name = "sampleSn", description = "샘플 순번", example = "1", in = ParameterIn.QUERY, schema = @Schema(implementation = Long.class))
            @RequestParam Long sampleSn
    ) throws Exception {
        BaseResponse baseResponse;

        // 조회용 dto set
        SampleDetailRequestDTO dto = SampleDetailRequestDTO.builder()
                .sampleSn(sampleSn)
                .build();

        // Sample 단건 조회
        SampleResponseDTO result = sampleService.getSample(dto);

        // response set
        baseResponse = !ObjectUtils.isEmpty(result)
                ? BaseResponse.getBaseResponseBuilder(HttpStatus.OK.value(), ResponseMessageConst.SELECT_SUCCESS, 1, result)
                : BaseResponse.getBaseResponseBuilder(HttpStatus.NO_CONTENT.value(), ResponseMessageConst.NO_CONTENT, 0, SampleResponseDTO.builder().build());

        return baseResponse;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = SampleResponseDTO.class))),
            @ApiResponse(responseCode = "204", description = "내용 없음", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @Operation(summary = "샘플 목록 조회", description = "샘플 목록 조회 API (제목, 내용이 없을 경우 전체목록 조회)")
    @GetMapping("list")
    public BaseResponse getSampleList(
            @Parameter(name = "title", description = "샘플 제목", example = "title1", in = ParameterIn.QUERY, schema = @Schema(implementation = String.class))
            @RequestParam(required = false) @ValidTitle String title,
            @Parameter(name = "content", description = "샘플 내용", example = "content1", in = ParameterIn.QUERY, schema = @Schema(implementation = String.class))
            @RequestParam(required = false) @ValidContent String content
    ) throws Exception {
        BaseResponse baseResponse;

        // 조회용 dto set
        SampleListRequestDTO dto = SampleListRequestDTO.builder()
                .title(ObjectUtils.isEmpty(title) ? null : title)
                .content(ObjectUtils.isEmpty(content) ? null : content)
                .build();

        // Sample 목록 조회
        List<SampleResponseDTO> resultList = sampleService.getSampleList(dto);

        // response set
        baseResponse = !ObjectUtils.isEmpty(resultList)
                ? BaseResponse.getBaseResponseBuilder(HttpStatus.OK.value(), ResponseMessageConst.SELECT_SUCCESS, resultList.size(), resultList)
                : BaseResponse.getBaseResponseBuilder(HttpStatus.NO_CONTENT.value(), ResponseMessageConst.NO_CONTENT, 0, SampleResponseDTO.builder().build());

        return baseResponse;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "204", description = "수정 실패", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @Operation(summary = "샘플 수정", description = "샘플 수정 API")
    @PatchMapping("")
    public BaseResponse updateSample(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "json",
                    content = @Content(schema = @Schema(implementation = SampleUpdateRequestDTO.class)))
            @RequestBody @Valid SampleUpdateRequestDTO dto
    ) throws Exception {
        BaseResponse baseResponse;

        // Sample 수정
        boolean result = sampleServiceTx.updateSample(dto);

        // response set
        baseResponse = result
                ? BaseResponse.getBaseResponseBuilder(HttpStatus.OK.value(), ResponseMessageConst.UPDATE_SUCCESS, 1, true)
                : BaseResponse.getBaseResponseBuilder(HttpStatus.NO_CONTENT.value(), ResponseMessageConst.UPDATE_FAIL, 0, false);

        return baseResponse;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "204", description = "수정 실패", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @Operation(summary = "샘플 삭제", description = "샘플 삭제 API")
    @DeleteMapping("")
    public BaseResponse deleteSample(
            @Parameter(name = "sampleSn", description = "샘플 순번", example = "1", in = ParameterIn.QUERY, schema = @Schema(implementation = Long.class))
            @RequestParam Long sampleSn
    ) throws Exception {
        BaseResponse baseResponse;

        // 삭제용 dto set
        SampleDeleteRequestDTO dto = SampleDeleteRequestDTO.builder()
                .sampleSn(sampleSn)
                .build();

        // Sample 삭제
        boolean result = sampleServiceTx.deleteSample(dto);

        // response set
        baseResponse = result
                ? BaseResponse.getBaseResponseBuilder(HttpStatus.OK.value(), ResponseMessageConst.DELETE_SUCCESS, 1, true)
                : BaseResponse.getBaseResponseBuilder(HttpStatus.NO_CONTENT.value(), ResponseMessageConst.DELETE_FAIL, 0, false);

        return baseResponse;
    }
}
