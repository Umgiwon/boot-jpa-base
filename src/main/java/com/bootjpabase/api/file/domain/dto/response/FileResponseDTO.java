package com.bootjpabase.api.file.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Schema(description = "첨부파일 응답 DTO")
@Builder
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FileResponseDTO implements Serializable {

    @Schema(description = "첨부파일 순번", example = "1")
    private Long fileSn;

    @Schema(description = "원본 파일명", example = "원본 파일명")
    private String realFileNm;

    @Schema(description = "저장 파일명", example = "저장 파일명")
    private String saveFileNm;

    @Schema(description = "파일경로", example = "파일경로")
    private String filePath;

    @Schema(description = "파일크기", example = "파일크기")
    private Long fileSize;

    @QueryProjection
    public FileResponseDTO(Long fileSn, String realFileNm, String saveFileNm, String filePath, Long fileSize) {
        this.fileSn = fileSn;
        this.realFileNm = realFileNm;
        this.saveFileNm = saveFileNm;
        this.filePath = filePath;
        this.fileSize = fileSize;
    }
}
