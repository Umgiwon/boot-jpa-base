package com.bootjpabase.carenation.api.sample.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Schema(description = "샘플 수정 요청 DTO")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // json 데이터를 java 객체로 역직렬화 할 때 매핑되지 않은 필드를 무시
public class SampleUpdateRequestDTO {

    @Schema(description = "샘플 순번", example = "1")
    private Long sampleSn;

    @Length(max = 30, message = "제목은 30글자 이하로 입력해야 합니다.")
    @Schema(description = "제목 입력값", example = "샘플의 수정할 제목")
    private String title;

    @Length(max = 150, message = "내용은 150글자 이하로 입력해야 합니다.")
    @Schema(description = "내용 입력값", example = "샘플의 수정할 내용")
    private String content;
}
