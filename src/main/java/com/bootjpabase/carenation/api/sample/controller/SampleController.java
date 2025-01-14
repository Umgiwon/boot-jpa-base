package com.bootjpabase.carenation.api.sample.controller;

import com.bootjpabase.carenation.api.sample.domain.dto.request.SampleListRequestDTO;
import com.bootjpabase.carenation.api.sample.domain.dto.response.SampleResponseDTO;
import com.bootjpabase.carenation.api.sample.service.SampleService;
import com.bootjpabase.carenation.api.sample.service.SampleServiceTx;
import com.bootjpabase.carenation.global.constant.ResponseMessageConst;
import com.bootjpabase.carenation.global.domain.dto.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

        /*
        // 조회용 dto set
        SampleListRequestDTO dto = SampleListRequestDTO.builder()
                .title(title)
                .content(content)
                .build();
         */

        // 조회용 dto set
        SampleListRequestDTO dto2 = new SampleListRequestDTO();

        // 제목이 있을 때
        if(!ObjectUtils.isEmpty(title)){
            dto2.setTitle(title);
        }

        // 내용이 있을 때
        if(!ObjectUtils.isEmpty(content)){
            dto2.setContent(content);
        }

        // Sample 목록 조회
        List<SampleResponseDTO> resultList = sampleService.getSampleList(dto2);

        // response set
        baseResponse = ObjectUtils.isEmpty(resultList)
                ? BaseResponse.getBaseResponseBuilder(HttpStatus.NO_CONTENT.value(), ResponseMessageConst.NO_CONTENT, SampleResponseDTO.builder().build(), 0)
                : BaseResponse.getBaseResponseBuilder(HttpStatus.OK.value(), ResponseMessageConst.SELECT_SUCCESS, resultList, resultList.size());

        return baseResponse;
    }

}
