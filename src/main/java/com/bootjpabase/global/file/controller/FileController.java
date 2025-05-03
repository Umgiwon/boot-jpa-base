package com.bootjpabase.global.file.controller;

import com.bootjpabase.global.domain.dto.BaseResponse;
import com.bootjpabase.global.domain.dto.BaseResponseFactory;
import com.bootjpabase.global.exception.ExceptionMsg;
import com.bootjpabase.global.file.domain.dto.response.FileResponseDTO;
import com.bootjpabase.global.file.service.FileService;
import com.bootjpabase.global.file.service.FileServiceTx;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "File API", description = "첨부파일 API")
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/file")
public class FileController {

    private final FileService fileService;
    private final FileServiceTx fileServiceTx;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "409", description = "데이터 중복", content = @Content(schema = @Schema(implementation = ExceptionMsg.class))),
            @ApiResponse(responseCode = "500", description = "서버내부 오류발생", content = @Content(schema = @Schema(implementation = ExceptionMsg.class)))
    })
    @Operation(summary = "첨부파일 삭제", description = "첨부파일 삭제 API")
    @DeleteMapping("/{fileSn}")
    public BaseResponse<FileResponseDTO> deleteFile(
            @PathVariable("fileSn") Long fileSn
    ) throws Exception {
        return BaseResponseFactory.success(fileServiceTx.deleteFile(fileSn));
    }
}
