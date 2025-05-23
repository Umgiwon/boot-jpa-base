package com.bootjpabase.api.user.domain.entity;


import com.bootjpabase.global.domain.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "TB_USER")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_SN", nullable = false, updatable = false)
    @Comment("사용자 순번")
    private Long userSn;

    @Column(name = "USER_ID", nullable = false, updatable = false, length = 30)
    @Comment("사용자 아이디")
    private String userId;

    @Column(name = "USER_NAME", nullable = false, length = 50)
    @Comment("사용자 이름")
    private String userName;

    @Column(name = "USER_PASSWORD", nullable = false, length = 100)
    @Comment("사용자 비밀번호")
    private String userPassword;

    @Column(name = "USER_PHONE", nullable = false, length = 11)
    @Comment("사용자 전화번호")
    private String userPhone;

    @Column(name = "USER_EMAIL", nullable = false, length = 200)
    @Comment("사용자 이메일")
    private String userEmail;

    @Column(name = "PROFILE_IMG_FILE_SN")
    @Comment("프로필 파일순번")
    private Long profileImgFileSn;
}
