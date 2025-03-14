package com.bootjpabase.api.user.repository;

import com.bootjpabase.api.user.domain.dto.request.UserListRequestDTO;
import com.bootjpabase.api.user.domain.dto.response.QUserResponseDTO;
import com.bootjpabase.api.user.domain.dto.response.UserResponseDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.bootjpabase.api.user.domain.entity.QUser.user;


@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    /**
     * 관리자 목록 조회
     * @param dto
     * @param pageable
     * @return
     */
    public Page<UserResponseDTO> getUserList(UserListRequestDTO dto, Pageable pageable) {
        List<UserResponseDTO> resultList;

        resultList = queryFactory
                .select(
                        new QUserResponseDTO(
                                user.userSn
                                , user.userId
                                , user.userName
                                , user.userPhone
                                , user.userEmail
                                , user.createdUser
                                , user.createdDate
                        )
                )
                .from(user)
                .where(pagingCondition(dto))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(user.createdDate.desc())
                .fetch();

        // 전체 데이터 카운트
        JPAQuery<Long> countQuery = queryFactory
                .select(user.count())
                .from(user)
                .where(pagingCondition(dto));

        return PageableExecutionUtils.getPage(resultList, pageable, countQuery::fetchOne);
    }

    /* ******************* 동적 쿼리를 위한 BooleanExpression *******************/

    /**
     * 페이징 처리시 조건절
     * @param dto
     * @return
     */
    private BooleanBuilder pagingCondition(UserListRequestDTO dto) {
        BooleanBuilder builder = new BooleanBuilder();

        if(dto.getUserName() != null) {
            builder.and(eqName(dto.getUserName()));
        }

        return builder;
    }

    /**
     * 관리자명 조회 조건
     * @param name
     * @return
     */
    private BooleanExpression eqName(String name) {
        return (!StringUtils.isEmpty(name)) ? user.userName.contains(name) : null;
    }
}
