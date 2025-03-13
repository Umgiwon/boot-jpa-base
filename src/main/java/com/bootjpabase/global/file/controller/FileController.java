package com.bootjpabase.global.file.controller;

import com.bootjpabase.global.constant.ResponseMessageConst;
import com.bootjpabase.global.domain.dto.BaseResponse;
import com.bootjpabase.global.file.service.FileService;
import com.bootjpabase.global.file.service.FileServiceTx;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "File API", description = "첨부파일 API")
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/file/")
public class FileController {

    private final FileService fileService;
    private final FileServiceTx fileServiceTx;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "204", description = "삭제 실패", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @Operation(summary = "첨부파일 삭제", description = "첨부파일 삭제 API")
    @DeleteMapping("delete")
    public BaseResponse deleteFile(
            @Parameter(name = "fileSn", description = "첨부파일 순번", example = "1", in = ParameterIn.QUERY, schema = @Schema(implementation = Long.class))
            @RequestParam Long fileSn
    ) throws Exception {
        BaseResponse baseResponse;

        // 첨부파일 삭제
        boolean result = fileServiceTx.deleteFile(fileSn);

        baseResponse = result
                ? BaseResponse.getBaseResponseBuilder(HttpStatus.OK.value(), ResponseMessageConst.DELETE_SUCCESS, 1, true)
                : BaseResponse.getBaseResponseBuilder(HttpStatus.BAD_REQUEST.value(), ResponseMessageConst.DELETE_FAIL, 0, false);

        return baseResponse;
    }
}
