package com.bootjpabase.api.file.domain.entity;

import com.bootjpabase.global.domain.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;


@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "TB_FILE")
public class File extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FILE_SN", updatable = false, nullable = false)
    @Comment("첨부파일 순번")
    private Long fileSn;

    @Column(name = "REAL_FILE_NM", length = 200, nullable = false)
    @Comment("원본파일명")
    private String realFileNm;

    @Column(name = "SAVE_FILE_NM", length = 200, nullable = false)
    @Comment("저장파일명")
    private String saveFileNm;

    @Column(name = "FILE_PATH", length = 200, nullable = false)
    @Comment("파일경로")
    private String filePath;

    @Column(name = "FILE_SIZE", nullable = false)
    @Comment("파일크기")
    private Long fileSize;
}
