package com.bootjpabase.api.sample.controller;

import com.bootjpabase.api.sample.domain.dto.request.SampleListRequestDTO;
import com.bootjpabase.api.sample.domain.dto.request.SampleSaveRequestDTO;
import com.bootjpabase.api.sample.domain.dto.request.SampleUpdateRequestDTO;
import com.bootjpabase.api.sample.domain.dto.response.SampleResponseDTO;
import com.bootjpabase.api.sample.service.SampleService;
import com.bootjpabase.api.sample.service.SampleServiceTx;
import com.bootjpabase.global.domain.dto.BaseResponse;
import com.bootjpabase.global.domain.dto.BaseResponseFactory;
import com.bootjpabase.global.exception.ExceptionMsg;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.bootjpabase.global.constant.SwaggerExampleConst.*;

@Tag(name = "Sample API", description = "샘플 API")
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/sample")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공", useReturnTypeSchema = true),
        @ApiResponse(responseCode = "400", description = "입력값 유효성 검증 실패", content = @Content(schema = @Schema(implementation = ExceptionMsg.class))),
        @ApiResponse(responseCode = "404", description = "데이터 오류", content = @Content(schema = @Schema(implementation = ExceptionMsg.class))),
        @ApiResponse(responseCode = "409", description = "데이터 중복", content = @Content(schema = @Schema(implementation = ExceptionMsg.class))),
        @ApiResponse(responseCode = "500", description = "서버내부 오류발생", content = @Content(schema = @Schema(implementation = ExceptionMsg.class)))
})
public class SampleController {

    private final SampleService sampleService; // 조회 전용
    private final SampleServiceTx sampleServiceTx; // 생성 | 수정 | 삭제 전용

    @Operation(summary = "샘플 단건 저장", description = "샘플 단건 저장 API")
    @PostMapping("")
    public BaseResponse<SampleResponseDTO> saveSample(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "json",
                content = @Content(
                    examples = {
                        @ExampleObject(name = "저장 예제1", value = SAMPLE_SAVE_EXAMPLE_1),
                        @ExampleObject(name = "저장 예제2", value = SAMPLE_SAVE_EXAMPLE_2)
                    }
                )
            )
            @Valid @RequestBody SampleSaveRequestDTO dto) {
        return BaseResponseFactory.success(sampleServiceTx.saveSample(dto));
    }

    @Operation(summary = "샘플 다건 저장", description = "샘플 다건 저장 API")
    @PostMapping("/saveList")
    public BaseResponse<List<SampleResponseDTO>> saveSampleList(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "json",
                content = @Content(
                    examples = {
                        @ExampleObject(name = "목록 저장 예제1", value = SAMPLE_SAVE_LIST_EXAMPLE_1)
                    }
                )
            )
            @Valid @RequestBody List<SampleSaveRequestDTO> dto) {
        return BaseResponseFactory.success(sampleServiceTx.saveAllSample(dto));
    }

    @Operation(summary = "샘플 단건 조회", description = "샘플 단건 조회 API")
    @GetMapping("/{sampleSn}")
    public BaseResponse<SampleResponseDTO> getSample(@PathVariable("sampleSn") Long sampleSn) {
        return BaseResponseFactory.success(sampleService.getSample(sampleSn));
    }

    @Operation(summary = "샘플 목록 조회", description = "샘플 목록 조회 API (제목, 내용이 없을 경우 전체목록 조회)")
    @GetMapping("")
    public BaseResponse<List<SampleResponseDTO>> getSampleList(
            @ParameterObject SampleListRequestDTO dto,
            @PageableDefault(page = 0, size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return BaseResponseFactory.success(sampleService.getSampleList(dto, pageable));
    }

    @Operation(summary = "샘플 수정", description = "샘플 수정 API")
    @PatchMapping("/{sampleSn}")
    public BaseResponse<SampleResponseDTO> updateSample(
            @PathVariable("sampleSn") Long sampleSn,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "json",
                content = @Content(
                    examples = {
                        @ExampleObject(name = "수정 예제1", value = SAMPLE_UPDATE_EXAMPLE_1),
                        @ExampleObject(name = "수정 예제2", value = SAMPLE_UPDATE_EXAMPLE_2)
                    }
                )
            )
            @RequestBody @Valid SampleUpdateRequestDTO dto) {
        return BaseResponseFactory.success(sampleServiceTx.updateSample(sampleSn, dto));
    }

    @Operation(summary = "샘플 삭제", description = "샘플 삭제 API")
    @DeleteMapping("/{sampleSn}")
    public BaseResponse<SampleResponseDTO> deleteSample(@PathVariable("sampleSn") Long sampleSn) {
        return BaseResponseFactory.success(sampleServiceTx.deleteSample(sampleSn));
    }
}
