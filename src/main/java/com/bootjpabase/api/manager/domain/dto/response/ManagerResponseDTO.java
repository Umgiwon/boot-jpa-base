package com.bootjpabase.api.manager.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Schema(description = "관리자 조회 응답 DTO")
@Builder
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // json 데이터를 java 객체로 역직렬화 할 때 매핑되지 않은 필드를 무시
public class ManagerResponseDTO implements Serializable {

    @Schema(description = "아이디", example = "admin")
    private String id;

    @Schema(description = "이름", example = "고수일 대표사무사")
    private String name;

    @Schema(description = "전화번호", example = "01011112222")
    private String phone;

    @Schema(description = "이메일", example = "test@test.com")
    private String email;

    @Schema(description = "관리자등급코드", example = "001")
    private String roleCode;

    @Schema(description = "관리등급명", example = "최고관리자")
    private String roleCodeName;

    @Schema(description = "등록자", example = "admin")
    private String regUser;

    @Schema(description = "등록일", example = "2025-02-21 17:56:25")
    private LocalDateTime regDt;

    @QueryProjection
    public ManagerResponseDTO(String id, String name, String phone, String email, String roleCode, String roleCodeName, String regUser, LocalDateTime regDt) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.roleCode = roleCode;
        this.roleCodeName = roleCodeName;
        this.regUser = regUser;
        this.regDt = regDt;
    }
}
