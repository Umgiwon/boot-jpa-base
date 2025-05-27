package com.bootjpabase.api.user.repository;

import com.bootjpabase.api.user.domain.dto.request.UserListRequestDTO;
import com.bootjpabase.api.user.domain.dto.response.QUserResponseDTO;
import com.bootjpabase.api.user.domain.dto.response.UserResponseDTO;
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
import java.util.Objects;
import java.util.stream.Stream;

import static com.bootjpabase.api.user.domain.entity.QUser.user;


@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    /**
     * User 목록 조회
     *
     * @param dto      조회할 User 조건 dto
     * @param pageable 페이징 조건
     * @return 조회된 User 목록 응답 dto
     */
    public Page<UserResponseDTO> getUserList(UserListRequestDTO dto, Pageable pageable) {
        List<UserResponseDTO> resultList = queryFactory
                .select(new QUserResponseDTO(
                        user.userSn
                        , user.userId
                        , user.userName
                        , user.userPhone
                        , user.userEmail
                        , user.createdUser
                        , user.createdDate
                ))
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
     *
     * @param dto 조회할 User 조건 dto
     * @return 페이징 처리시 조건절
     */
    private BooleanExpression pagingCondition(UserListRequestDTO dto) {
        return Stream.of(
                        containsName(dto.getUserName()) // 이름
                )
                .filter(Objects::nonNull)
                .reduce(BooleanExpression::and)
                .orElse(null);
    }

    /**
     * User 조회시 이름 비교
     *
     * @param name 조회할 이름
     * @return 조회할 이름 조건절
     */
    private BooleanExpression containsName(String name) {
        return (!StringUtils.isEmpty(name)) ? user.userName.contains(name) : null;
    }
}
