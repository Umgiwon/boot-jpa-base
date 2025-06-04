package com.bootjpabase.api.car.repository;

import com.bootjpabase.api.car.domain.dto.request.CarListRequestDTO;
import com.bootjpabase.api.car.domain.dto.response.CarResponseDTO;
import com.bootjpabase.api.car.domain.dto.response.QCarResponseDTO;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang3.StringUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static com.bootjpabase.api.car.domain.entity.QCar.car;


@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CarRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    /**
     * Car 목록 조회
     *
     * @param dto      조회할 Car 조건 dto
     * @param pageable 페이징 조건
     * @return 조회된 Car 목록 응답 dto
     */
    public Page<CarResponseDTO> getCarList(CarListRequestDTO dto, Pageable pageable) {
        List<CarResponseDTO> resultList = queryFactory
                .select(new QCarResponseDTO(
                        car.carSn
                        , car.category
                        , car.manufacturer
                        , car.modelName
                        , car.productionYear
                        , car.rentalYn
                        , car.rentalDescription
                ))
                .from(car)
                .where(pagingCondition(dto))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(car.carSn.asc())
                .fetch();

        // 전체 데이터 카운트
        JPAQuery<Long> countQuery = queryFactory
                .select(car.count())
                .from(car)
                .where(pagingCondition(dto));

        return PageableExecutionUtils.getPage(resultList, pageable, countQuery::fetchOne);
    }

    /* ==================== 동적 쿼리를 위한 BooleanExpression ==================== */

    /**
     * 페이징 처리시 조건절
     *
     * @param dto 조회할 Car 조건 dto
     * @return 페이징 처리시 조건절
     */
    private BooleanExpression pagingCondition(CarListRequestDTO dto) {
        return Stream.of(
                        containsCategory(dto.getCategory()), // 카테고리
                        containsManufacturer(dto.getManufacturer()), // 제조사
                        containsModelName(dto.getModelName()), // 모델명
                        eqProductionYear(dto.getProductionYear()), // 제조년도
                        eqRentalYn(dto.getRentalYn()) // 대여가능여부
                )
                .filter(Objects::nonNull)
                .reduce(BooleanExpression::and)
                .orElse(null);
    }

    /**
     * Car 조회 시
     *
     * @param category 조회할 카테고리
     * @return 조회할 카테고리 조건절
     */
    private BooleanExpression containsCategory(String category) {
        return StringUtils.isNotBlank(category) ? car.category.contains(category) : null;
    }

    /**
     * Car 조회 시 제조사 비교
     *
     * @param manufacturer 조회할 제조사
     * @return 조회할 제조사 조건절
     */
    private BooleanExpression containsManufacturer(String manufacturer) {
        return StringUtils.isNotBlank(manufacturer) ? car.manufacturer.contains(manufacturer) : null;
    }

    /**
     * Car 조회 시 모델명 비교
     *
     * @param modelName 조회할 모델명
     * @return 조회할 모델명 조건절
     */
    private BooleanExpression containsModelName(String modelName) {
        return StringUtils.isNotBlank(modelName) ? car.modelName.contains(modelName) : null;
    }

    /**
     * Car 조회 시 생산년도 비교
     *
     * @param productionYear 조회할 생산년도
     * @return 조회할 생산년도 조건절
     */
    private BooleanExpression eqProductionYear(Integer productionYear) {
        return !ObjectUtils.isEmpty(productionYear) ? car.productionYear.eq(productionYear) : null;
    }

    /**
     * Car 조회 시 대여 가능 여부 비교
     *
     * @param rentalYn 조회할 대여 가능 여부
     * @return 조회할 대여 가능 여부 조건절
     */
    private BooleanExpression eqRentalYn(String rentalYn) {
        return StringUtils.isNotBlank(rentalYn) ? car.rentalYn.eq(rentalYn) : null;
    }
}
