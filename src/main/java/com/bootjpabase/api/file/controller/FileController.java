package com.bootjpabase.api.file.controller;

import com.bootjpabase.api.file.domain.dto.response.FileResponseDTO;
import com.bootjpabase.api.file.service.FileServiceTx;
import com.bootjpabase.global.domain.dto.BaseResponse;
import com.bootjpabase.global.domain.dto.BaseResponseFactory;
import io.swagger.v3.oas.annotations.Operation;
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

    private final FileServiceTx fileServiceTx;

    @Operation(summary = "첨부파일 삭제", description = "첨부파일 순번으로 삭제")
    @DeleteMapping("/{fileSn}")
    public BaseResponse<FileResponseDTO> deleteFile(
            @PathVariable("fileSn") Long fileSn
    ) {
        return BaseResponseFactory.success(fileServiceTx.deleteFile(fileSn));
    }
}
