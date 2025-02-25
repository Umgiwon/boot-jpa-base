package com.bootjpabase.api.manager.repository;

import com.bootjpabase.api.manager.domain.dto.request.ManagerListRequestDTO;
import com.bootjpabase.api.manager.domain.dto.response.ManagerDetailResponseDTO;
import com.bootjpabase.api.manager.domain.dto.response.ManagerResponseDTO;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ManagerRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    /**
     * 관리자 목록 조회
     * @param dto
     * @return
     */
    public List<ManagerResponseDTO> getManagerList(ManagerListRequestDTO dto, Pageable pageable) {
        List<ManagerResponseDTO> resultList;

        resultList = queryFactory
                .select(
                        new QManagerResponseDTO(
                                manager.id
                                , manager.name
                                , manager.phone
                                , manager.email
                                , manager.roleCode
                                , JPAExpressions
                                    .select(code1.codeName)
                                    .from(code1)
                                    .where(code1.code.eq(manager.roleCode))
                                , manager.regUser
                                , manager.regDt
                        )
                )
                .from(manager)
                .where(eqName(dto.getName()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(manager.regDt.desc())
                .fetch();

        return resultList;
    }

    /**
     * 관리자 상세 조회
     * @param id
     * @return
     */
    public ManagerDetailResponseDTO getManager(String id) {
        ManagerDetailResponseDTO result;

        result = queryFactory
                .select(
                        new QManagerDetailResponseDTO(
                                manager.id
                                , manager.name
                                , manager.phone
                                , manager.email
                                , manager.roleCode
                                , JPAExpressions
                                    .select(code1.codeName)
                                    .from(code1)
                                    .where(code1.code.eq(manager.roleCode))
                                , manager.regUser
                                , manager.regDt
                        )
                )
                .from(manager)
                .where(manager.id.eq(id))
                .fetchOne();

        return result;
    }

    /* ******************* 동적 쿼리를 위한 BooleanExpression *******************/

    /**
     * 관리자명 조회 조건
     * @param name
     * @return
     */
    private BooleanExpression eqName(String name) {
        return (!StringUtils.isEmpty(name)) ? manager.name.contains(name) : null;
    }
}
