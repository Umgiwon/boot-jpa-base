package com.bootjpabase.api.sample.repository;

import com.bootjpabase.api.sample.domain.dto.request.SampleListRequestDTO;
import com.bootjpabase.api.sample.domain.dto.response.QSampleResponseDTO;
import com.bootjpabase.api.sample.domain.dto.response.SampleResponseDTO;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.bootjpabase.api.sample.domain.entity.QSample.sample;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SampleRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    /**
     * Sample 단건 조회
     * @param sampleSn
     * @return
     */
    public SampleResponseDTO getSample(Long sampleSn) {
        SampleResponseDTO result;

        result = queryFactory
                .select(
                        new QSampleResponseDTO(
                                sample.sampleSn
                                , sample.title
                                , sample.content
                        )
                )
                .from(sample)
                .where(sample.sampleSn.eq(sampleSn))
                .fetchOne();

        return result;
    }

    /**
     * Sample 목록 조회
     * @param dto
     * @return
     */
    public List<SampleResponseDTO> getSampleList(SampleListRequestDTO dto) {
        List<SampleResponseDTO> resultList;

        resultList = queryFactory
                .select(
                        new QSampleResponseDTO(
                                sample.sampleSn
                                , sample.title
                                , sample.content
                        )
                )
                .from(sample)
                .where(eqTitle(dto.getTitle())
                        , eqContent(dto.getContent())
                )
                .orderBy(sample.sampleSn.asc())
                .fetch();

        return resultList;
    }

    /* ******************* 동적 쿼리를 위한 BooleanExpression *******************/

    /**
     * Sample 조회 시 제목 비교
     * @param title
     * @return
     */
    private BooleanExpression eqTitle(String title) {
        return (!StringUtils.isEmpty(title)) ? sample.title.eq(title) : null;
    }

    /**
     * Sample 조회 시 내용 비교
     * @param content
     * @return
     */
    private BooleanExpression eqContent(String content) {
        return (!StringUtils.isEmpty(content)) ? sample.content.eq(content) : null;
    }
}
