package com.bootjpabase.api.sample.controller;

import com.bootjpabase.api.sample.domain.dto.request.SampleListRequestDTO;
import com.bootjpabase.api.sample.domain.dto.request.SampleSaveRequestDTO;
import com.bootjpabase.api.sample.domain.dto.request.SampleUpdateRequestDTO;
import com.bootjpabase.api.sample.domain.dto.response.SampleResponseDTO;
import com.bootjpabase.api.sample.service.SampleService;
import com.bootjpabase.api.sample.service.SampleServiceTx;
import com.bootjpabase.global.constant.ResponseMessageConst;
import com.bootjpabase.global.domain.dto.BaseResponse;
import com.bootjpabase.global.domain.dto.Pagination;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "Sample API", description = "샘플 API")
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/sample")
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
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "json", content = @Content(
                    examples = {
                            @ExampleObject(name = "저장 예제1", value = """
                                      {
                                          "title": "샘플 제목1",
                                          "content": "샘플 내용1"
                                      }
                            """),
                            @ExampleObject(name = "저장 예제2", value = """
                                      {
                                         "title": "샘플 제목2",
                                          "content": "샘플 내용2"
                                      }
                            """)
                    }
            ))
            @Valid @RequestBody SampleSaveRequestDTO dto
    ) throws Exception {
        BaseResponse baseResponse;

        // Sample 단건 저장
        boolean result = sampleServiceTx.saveSample(dto);

        // response set
        baseResponse = result
                ? BaseResponse.getBaseResponseBuilder(HttpStatus.OK.value(), ResponseMessageConst.SAVE_SUCCESS, 1 , true)
                : BaseResponse.getBaseResponseBuilder(HttpStatus.BAD_REQUEST.value(), ResponseMessageConst.SAVE_FAIL, 0, false);

        return baseResponse;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "저장 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "204", description = "저장 실패", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @Operation(summary = "샘플 다건 저장", description = "샘플 다건 저장 API")
    @PostMapping("/list")
    public BaseResponse saveSampleList(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "json", content = @Content(
                    examples = {
                            @ExampleObject(name = "예제 1", value = """
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
            @Valid @RequestBody List<SampleSaveRequestDTO> dto
    ) throws Exception {
        BaseResponse baseResponse;

        // Sample 다건 저장
        boolean result = sampleServiceTx.saveAllSample(dto);

        // response set
        baseResponse = result
                ? BaseResponse.getBaseResponseBuilder(HttpStatus.OK.value(), ResponseMessageConst.SAVE_SUCCESS, dto.size(), true)
                : BaseResponse.getBaseResponseBuilder(HttpStatus.BAD_REQUEST.value(), ResponseMessageConst.SAVE_FAIL, 0, false);

        return baseResponse;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = SampleResponseDTO.class))),
            @ApiResponse(responseCode = "204", description = "내용 없음", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @Operation(summary = "샘플 단건 조회", description = "샘플 단건 조회 API")
    @GetMapping("/{sampleSn}")
    public BaseResponse getSample(
            @PathVariable("sampleSn") Long sampleSn
    ) throws Exception {
        BaseResponse baseResponse;

        // Sample 단건 조회
        SampleResponseDTO result = sampleService.getSample(sampleSn);

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
    @GetMapping("")
    public BaseResponse getSampleList(
            @Parameter(name = "title", description = "샘플 제목", example = "title1", in = ParameterIn.QUERY, schema = @Schema(implementation = String.class))
            @RequestParam(required = false) String title,
            @Parameter(name = "content", description = "샘플 내용", example = "content1", in = ParameterIn.QUERY, schema = @Schema(implementation = String.class))
            @RequestParam(required = false) String content,
            @PageableDefault(page = 0, size = 10, sort = "regDt", direction = Sort.Direction.DESC) Pageable pageable
    ) throws Exception {
        BaseResponse baseResponse;

        // 조회용 dto set
        SampleListRequestDTO dto = SampleListRequestDTO.builder()
                .title(ObjectUtils.isEmpty(title) ? null : title)
                .content(ObjectUtils.isEmpty(content) ? null : content)
                .build();

        // Sample 목록 조회
        Page<SampleResponseDTO> resultPaging = sampleService.getSampleList(dto, pageable);
        List<SampleResponseDTO> resultList = resultPaging.getContent();

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
    @Operation(summary = "샘플 수정", description = "샘플 수정 API")
    @PatchMapping("/{sampleSn}")
    public BaseResponse updateSample(
            @PathVariable("sampleSn") Long sampleSn,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "json", content = @Content(
                    examples = {
                            @ExampleObject(name = "수정 예제1", value = """
                                      {
                                          "title": "샘플 제목 수정1",
                                          "content": "샘플 내용 수정1"
                                      }
                            """),
                            @ExampleObject(name = "수정 예제2", value = """
                                      {
                                         "title": "샘플 제목 수정2",
                                          "content": "샘플 내용 수정 2"
                                      }
                            """)
                    }
            ))
            @RequestBody @Valid SampleUpdateRequestDTO dto
    ) throws Exception {
        BaseResponse baseResponse;

        // Sample 수정
        boolean result = sampleServiceTx.updateSample(sampleSn, dto);

        // response set
        baseResponse = result
                ? BaseResponse.getBaseResponseBuilder(HttpStatus.OK.value(), ResponseMessageConst.UPDATE_SUCCESS, 1, true)
                : BaseResponse.getBaseResponseBuilder(HttpStatus.BAD_REQUEST.value(), ResponseMessageConst.UPDATE_FAIL, 0, false);

        return baseResponse;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "204", description = "수정 실패", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @Operation(summary = "샘플 삭제", description = "샘플 삭제 API")
    @DeleteMapping("/{sampleSn}")
    public BaseResponse deleteSample(
            @PathVariable("sampleSn") Long sampleSn
    ) throws Exception {
        BaseResponse baseResponse;

        // Sample 삭제
        boolean result = sampleServiceTx.deleteSample(sampleSn);

        // response set
        baseResponse = result
                ? BaseResponse.getBaseResponseBuilder(HttpStatus.OK.value(), ResponseMessageConst.DELETE_SUCCESS, 1, true)
                : BaseResponse.getBaseResponseBuilder(HttpStatus.BAD_REQUEST.value(), ResponseMessageConst.DELETE_FAIL, 0, false);

        return baseResponse;
    }
}
