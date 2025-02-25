package com.bootjpabase.api.manager.domain.entity;

import com.nangman.global.domain.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "TB_MANAGER")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Manager extends BaseEntity {

    @Id
    @Column(name = "ID", length = 30, updatable = false, nullable = false)
    @Comment("아이디")
    private String id;

    @Column(name = "PASSWORD", length = 100, nullable = false)
    @Comment("비밀번호")
    private String password;

    @Column(name = "NAME", length = 50, nullable = false)
    @Comment("이름")
    private String name;

    @Column(name = "PHONE", length = 11, nullable = false)
    @Comment("전화번호")
    private String phone;

    @Column(name = "EMAIL", length = 200, nullable = false)
    @Comment("이메일")
    private String email;

    @Column(name = "ROLE_CODE", length = 50, nullable = false)
    @Comment("관리자등급코드")
    private String roleCode;
}
