package com.bootjpabase.api.sample.repository;

import com.bootjpabase.api.sample.domain.dto.request.SampleListRequestDTO;
import com.bootjpabase.api.sample.domain.dto.response.QSampleResponseDTO;
import com.bootjpabase.api.sample.domain.dto.response.SampleResponseDTO;
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
import java.util.Objects;
import java.util.stream.Stream;

import static com.bootjpabase.api.sample.domain.entity.QSample.sample;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SampleRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    /**
     * Sample 상세 조회
     *
     * @param sampleSn 조회할 Sample 순번
     * @return 조회된 Sample 응답 dto
     */
    public SampleResponseDTO getSample(Long sampleSn) {
        return queryFactory
                .select(new QSampleResponseDTO(
                        sample.sampleSn
                        , sample.title
                        , sample.content
                ))
                .from(sample)
                .where(sample.sampleSn.eq(sampleSn))
                .fetchOne();
    }

    /**
     * Sample 목록 조회
     *
     * @param dto      조회할 Sample 조건 dto
     * @param pageable 페이징 조건
     * @return 조회된 Sample 목록 응답 dto
     */
    public Page<SampleResponseDTO> getSampleList(SampleListRequestDTO dto, Pageable pageable) {
        List<SampleResponseDTO> resultList = queryFactory
                .select(new QSampleResponseDTO(
                        sample.sampleSn
                        , sample.title
                        , sample.content
                ))
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
     * @return 페이징 처리시 조건절
     */
    private BooleanExpression pagingCondition(SampleListRequestDTO dto) {
        return Stream.of(
                        containsTitle(dto.getTitle()), // 제목
                        containsContent(dto.getContent()) // 내용
                )
                .filter(Objects::nonNull)
                .reduce(BooleanExpression::and)
                .orElse(null);
    }

    /**
     * Sample 조회시 제목 비교
     *
     * @param title 조회할 제목
     * @return 조회할 제목 조건절
     */
    private BooleanExpression containsTitle(String title) {
        return (!StringUtils.isNotBlank(title)) ? sample.title.contains(title) : null;
    }

    /**
     * Sample 조회시 내용 비교
     *
     * @param content 조회할 내용
     * @return 조회할 내용 조건절
     */
    private BooleanExpression containsContent(String content) {
        return (!StringUtils.isNotBlank(content)) ? sample.content.contains(content) : null;
    }
}
