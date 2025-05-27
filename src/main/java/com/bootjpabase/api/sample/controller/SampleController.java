package com.bootjpabase.api.sample.controller;

import com.bootjpabase.api.sample.domain.dto.request.SampleListRequestDTO;
import com.bootjpabase.api.sample.domain.dto.request.SampleSaveRequestDTO;
import com.bootjpabase.api.sample.domain.dto.request.SampleUpdateRequestDTO;
import com.bootjpabase.api.sample.domain.dto.response.SampleResponseDTO;
import com.bootjpabase.api.sample.service.SampleService;
import com.bootjpabase.api.sample.service.SampleServiceTx;
import com.bootjpabase.global.annotation.common.CustomApiLogger;
import com.bootjpabase.global.domain.dto.BaseResponse;
import com.bootjpabase.global.domain.dto.BaseResponseFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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
@CustomApiLogger
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/sample")
public class SampleController {

    private final SampleService sampleService; // 조회 전용
    private final SampleServiceTx sampleServiceTx; // 생성 | 수정 | 삭제 전용

    @Operation(summary = "샘플 저장", description = "샘플 저장 API <br>(단건 & 다건)")
    @PostMapping("")
    public BaseResponse<List<SampleResponseDTO>> saveSample(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "json", content = @Content(examples = {
                    @ExampleObject(name = "저장 예제1", value = SAMPLE_SAVE_EXAMPLE_1),
                    @ExampleObject(name = "목록 저장 예제1", value = SAMPLE_SAVE_LIST_EXAMPLE_1)
            }))
            @RequestBody @Valid List<SampleSaveRequestDTO> dto) {
        return BaseResponseFactory.success(sampleServiceTx.saveSample(dto));
    }

    @Operation(summary = "샘플 상세 조회", description = "샘플 상세 조회 API")
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
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "json", content = @Content(examples = {
                    @ExampleObject(name = "수정 예제1", value = SAMPLE_UPDATE_EXAMPLE_1)
            }))
            @RequestBody @Valid SampleUpdateRequestDTO dto) {
        return BaseResponseFactory.success(sampleServiceTx.updateSample(sampleSn, dto));
    }

    @Operation(summary = "샘플 삭제", description = "샘플 삭제 API")
    @DeleteMapping("/{sampleSn}")
    public BaseResponse<SampleResponseDTO> deleteSample(@PathVariable("sampleSn") Long sampleSn) {
        return BaseResponseFactory.success(sampleServiceTx.deleteSample(sampleSn));
    }
}
