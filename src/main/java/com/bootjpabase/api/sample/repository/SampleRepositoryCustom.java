package com.bootjpabase.api.sample.repository;

import com.bootjpabase.api.sample.domain.dto.request.SampleListRequestDTO;
import com.bootjpabase.api.sample.domain.dto.response.QSampleResponseDTO;
import com.bootjpabase.api.sample.domain.dto.response.SampleResponseDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
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
     *
     * @param sampleSn 조회할 Sample 순번
     * @return 조회된 Sample 응답 dto
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
     *
     * @param dto 조회할 Sample 조건 dto
     * @param pageable 페이징 조건
     * @return 조회된 Sample 목록 응답 dto
     */
    public Page<SampleResponseDTO> getSampleList(SampleListRequestDTO dto, Pageable pageable) {
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
                .where(pagingCondition(dto))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(sample.sampleSn.asc())
                .fetch();

        // 전체 데이터 카운트
        JPAQuery<Long> countQuery = queryFactory
                .select(sample.count())
                .from(sample)
                .where(pagingCondition(dto));

        return PageableExecutionUtils.getPage(resultList, pageable, countQuery::fetchOne);
    }

    /* ******************* 동적 쿼리를 위한 BooleanExpression *******************/

    /**
     * 페이징 처리시 조건절
     *
     * @param dto 조회할 Sample 조건 dto
     * @return 페이징 처리시 조건절 builder
     */
    private BooleanBuilder pagingCondition(SampleListRequestDTO dto) {
        BooleanBuilder builder = new BooleanBuilder();

        if(dto.getTitle() != null) {
            builder.and(eqTitle(dto.getTitle()));
        }

        if(dto.getContent() != null) {
            builder.and(eqContent(dto.getContent()));
        }

        return builder;
    }

    /**
     * Sample 조회시 제목 비교
     *
     * @param title 조회할 제목
     * @return 조회할 제목 조건절
     */
    private BooleanExpression eqTitle(String title) {
        return (!StringUtils.isEmpty(title)) ? sample.title.contains(title) : null;
    }

    /**
     * Sample 조회시 내용 비교
     *
     * @param content 조회할 내용
     * @return 조회할 내용 조건절
     */
    private BooleanExpression eqContent(String content) {
        return (!StringUtils.isEmpty(content)) ? sample.content.contains(content) : null;
    }
}
