package com.bootjpabase.api.sample.domain.entity;

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
@Table(name = "TB_SAMPLE")
public class Sample extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SAMPLE_SN", nullable = false, updatable = false)
    @Comment("샘플 순번")
    private Long sampleSn;

    @Column(name = "TITLE", nullable = false, length = 50)
    @Comment("샘플 제목")
    private String title;

    @Column(name = "CONTENT", nullable = false, length = 100)
    @Comment("샘플 내용")
    private String content;
}
