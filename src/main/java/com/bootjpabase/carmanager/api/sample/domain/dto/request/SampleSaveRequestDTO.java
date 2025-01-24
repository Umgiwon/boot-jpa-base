package com.bootjpabase.carmanager.api.sample.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Schema(description = "샘플 저장 요청 DTO")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // json 데이터를 java 객체로 역직렬화 할 때 매핑되지 않은 필드를 무시
public class SampleSaveRequestDTO {

    @NotBlank(message = "제목은 필수입니다")
    @Length(max = 30, message = "제목은 30글자 이하로 입력해야 합니다.")
    @Schema(description = "제목 입력값", example = "샘플의 저장할 제목")
    private String title;

    @NotBlank(message = "내용은 필수입니다")
    @Length(max = 150, message = "내용은 150글자 이하로 입력해야 합니다.")
    @Schema(description = "내용 입력값", example = "샘플의 저장할 내용")
    private String content;
}
