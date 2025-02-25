package com.bootjpabase.api.manager.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Schema(description = "관리자 수정 요청 DTO")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // json 데이터를 java 객체로 역직렬화 할 때 매핑되지 않은 필드를 무시
public class ManagerUpdateRequestDTO {

    @NotNull(message = "아이디는 필수입니다")
    @Schema(description = "아이디", example = "admin")
    private String id;

    @Length(max = 30, message = "비밀번호는 30자 이하로 입력해야 합니다.")
    @Schema(description = "비밀번호", example = "admin!")
    private String password;

    @Length(max = 11, min = 11, message = "휴대폰번호는 '-' 빼고 입력해주세요.")
    @Schema(description = "전화번호", example = "01011112222")
    private String phone;

    @Length(max = 200, message = "이메일은 200자 이하로 입력해야 합니다.")
    @Schema(description = "이메일", example = "test@test.com")
    private String email;

    @Length(max = 50, message = "관리자등급코드는 50자 이하로 입력해야 합니다.")
    @Schema(description = "관리자등급코드", example = "001")
    private String roleCode;
}
