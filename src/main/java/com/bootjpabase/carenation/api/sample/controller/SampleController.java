package com.bootjpabase.carenation.api.sample.controller;

import com.bootjpabase.carenation.api.sample.domain.dto.request.SampleDetailRequestDTO;
import com.bootjpabase.carenation.api.sample.domain.dto.request.SampleListRequestDTO;
import com.bootjpabase.carenation.api.sample.domain.dto.request.SampleSaveRequestDTO;
import com.bootjpabase.carenation.api.sample.domain.dto.response.SampleResponseDTO;
import com.bootjpabase.carenation.api.sample.service.SampleService;
import com.bootjpabase.carenation.api.sample.service.SampleServiceTx;
import com.bootjpabase.carenation.global.constant.ResponseMessageConst;
import com.bootjpabase.carenation.global.domain.dto.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Sample API", description = "Sample API 설명")
@RestController
@RequiredArgsConstructor
@RequestMapping("/sample/")
public class SampleController {

    private final SampleService sampleService;
    private final SampleServiceTx sampleServiceTx;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = SampleResponseDTO.class))),
            @ApiResponse(responseCode = "204", description = "내용 없음", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @Operation(summary = "샘플 목록 조회", description = "샘플 목록 조회 API (제목, 내용이 없을 경우 전체목록 조회)")
    @GetMapping("sampleList")
    public BaseResponse getSampleList(
            @Parameter(name = "title", description = "샘플 제목", example = "title1", in = ParameterIn.QUERY, schema = @Schema(implementation = String.class))
            @RequestParam(required = false) String title,
            @Parameter(name = "content", description = "샘플 내용", example = "content1", in = ParameterIn.QUERY, schema = @Schema(implementation = String.class))
            @RequestParam(required = false) String content
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
        baseResponse = ObjectUtils.isEmpty(resultList)
                ? BaseResponse.getBaseResponseBuilder(HttpStatus.NO_CONTENT.value(), ResponseMessageConst.NO_CONTENT, 0, SampleResponseDTO.builder().build())
                : BaseResponse.getBaseResponseBuilder(HttpStatus.OK.value(), ResponseMessageConst.SELECT_SUCCESS, resultList.size(), resultList);

        return baseResponse;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = SampleResponseDTO.class))),
            @ApiResponse(responseCode = "204", description = "내용 없음", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @Operation(summary = "샘플 단건 조회", description = "샘플 단건 조회 API")
    @GetMapping("sample")
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
        baseResponse = ObjectUtils.isEmpty(result)
                ? BaseResponse.getBaseResponseBuilder(HttpStatus.NO_CONTENT.value(), ResponseMessageConst.NO_CONTENT, 0, SampleResponseDTO.builder().build())
                : BaseResponse.getBaseResponseBuilder(HttpStatus.OK.value(), ResponseMessageConst.SELECT_SUCCESS, 1, result);

        return baseResponse;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "저장 성공", content = @Content(schema = @Schema(implementation = SampleResponseDTO.class))),
            @ApiResponse(responseCode = "204", description = "저장 실패", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @Operation(summary = "샘플 단건 저장", description = "샘플 단건 저장 API")
    @PostMapping("sample")
    public BaseResponse saveSample(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "json",
                    content = @Content(schema = @Schema(implementation = SampleSaveRequestDTO.class)))
            @org.springframework.web.bind.annotation.RequestBody SampleSaveRequestDTO dto
    ) throws Exception {
        BaseResponse baseResponse;

        // Sample 단건 저장
        boolean result = sampleServiceTx.saveSample(dto);

        // response set
        if(result) {
            baseResponse = BaseResponse.getBaseResponseBuilder(HttpStatus.OK.value(), ResponseMessageConst.SAVE_SUCCESS, 0 , null);
        } else {
            baseResponse = BaseResponse.getBaseResponseBuilder(HttpStatus.NO_CONTENT.value(), ResponseMessageConst.SAVE_FAIL, 0, null);
        }

        return baseResponse;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "저장 성공", content = @Content(schema = @Schema(implementation = SampleResponseDTO.class))),
            @ApiResponse(responseCode = "204", description = "저장 실패", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @PostMapping("sampleList")
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
            @org.springframework.web.bind.annotation.RequestBody List<SampleSaveRequestDTO> dto
    ) throws Exception {
        BaseResponse baseResponse;

        // Sample 다건 저장
        boolean result = sampleServiceTx.saveAllSample(dto);

        // response set
        if(result) {
            baseResponse = BaseResponse.getBaseResponseBuilder(HttpStatus.OK.value(), ResponseMessageConst.SAVE_SUCCESS, 0, null);
        } else {
            baseResponse = BaseResponse.getBaseResponseBuilder(HttpStatus.NO_CONTENT.value(), ResponseMessageConst.SAVE_FAIL, 0, null);
        }

        return baseResponse;
    }



}
