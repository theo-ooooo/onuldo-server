package com.onuldo.adapter.outbound.persistence.user

import com.onuldo.domain.user.QUser.user
import com.onuldo.domain.user.User
import com.onuldo.domain.user.UserStatus
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

/**
 * QueryDSL을 사용한 사용자 조회 리포지토리
 * 
 * 복잡한 쿼리나 동적 쿼리가 필요한 경우 이 리포지토리를 사용합니다.
 */
@Repository
class UserQueryRepository(
    private val queryFactory: JPAQueryFactory
) {
    /**
     * 활성 상태인 사용자 목록 조회
     */
    fun findActiveUsers(): List<User> {
        return queryFactory
            .selectFrom(user)
            .where(user.status.eq(UserStatus.ACTIVE))
            .fetch()
    }

    /**
     * 이메일로 활성 사용자 조회
     */
    fun findActiveUserByEmail(email: String): User? {
        return queryFactory
            .selectFrom(user)
            .where(
                user.email.eq(email)
                    .and(user.status.eq(UserStatus.ACTIVE))
            )
            .fetchOne()
    }

    /**
     * 닉네임으로 사용자 검색 (부분 일치)
     */
    fun searchUsersByNickname(nickname: String): List<User> {
        return queryFactory
            .selectFrom(user)
            .where(
                user.nickname.containsIgnoreCase(nickname)
                    .and(user.status.eq(UserStatus.ACTIVE))
            )
            .fetch()
    }
}

