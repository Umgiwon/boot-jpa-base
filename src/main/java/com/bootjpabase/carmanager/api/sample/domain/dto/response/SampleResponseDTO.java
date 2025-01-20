package com.bootjpabase.carmanager.api.sample.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Schema(description = "샘플 응답 DTO")
@Builder
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // json 데이터를 java 객체로 역직렬화 할 때 매핑되지 않은 필드를 무시
public class SampleResponseDTO implements Serializable {

    @Schema(description = "샘플 순번", example = "1")
    private Long sampleSn;

    @Schema(description = "제목 입력값", example = "조회된 샘플의 제목")
    private String title;

    @Schema(description = "내용 입력값", example = "조회된 샘플의 내용")
    private String content;

    @QueryProjection
    public SampleResponseDTO(Long sampleSn, String title, String content) {
        this.sampleSn = sampleSn;
        this.title = title;
        this.content = content;
    }
}
