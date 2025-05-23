package com.bootjpabase.api.user.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * DTO for {@link com.bootjpabase.api.user.domain.entity.User}
 */
@Schema(description = "사용자 수정 요청 DTO")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true) // json 데이터를 java 객체로 역직렬화 할 때 매핑되지 않은 필드를 무시
public class UserUpdateRequestDTO {

    @Length(max = 30, message = "비밀번호는 30자 이하로 입력해야 합니다.")
    @Schema(description = "비밀번호", example = "user!")
    private String userPassword;

    @Length(max = 11, min = 11, message = "휴대폰번호는 '-' 빼고 입력해주세요.")
    @Schema(description = "전화번호", example = "01011112222")
    private String userPhone;

    @Length(max = 200, message = "이메일은 200자 이하로 입력해야 합니다.")
    @Schema(description = "이메일", example = "test@test.com")
    private String userEmail;
}
