package com.bootjpabase.carmanager.api.sample.domain.entity;

import com.bootjpabase.carmanager.global.domain.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;


@Entity
@Table(name = "TB_SAMPLE")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Sample extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SAMPLE_SN", updatable = false, nullable = false)
    @Comment("샘플 순번")
    private Long sampleSn;

    @Column(name = "TITLE", nullable = false)
    @Comment("샘플 제목")
    private String title;

    @Column(name = "CONTENT", nullable = false)
    @Comment("샘플 내용")
    private String content;
}
