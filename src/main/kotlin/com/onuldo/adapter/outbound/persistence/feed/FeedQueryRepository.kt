package com.onuldo.adapter.outbound.persistence.feed

import com.onuldo.domain.hobby.QHobby
import com.onuldo.domain.record.QRecord
import com.onuldo.domain.record.Record
import com.onuldo.domain.record.RecordVisibility
import com.onuldo.domain.tag.QRecordTag
import com.onuldo.domain.tag.QTag
import com.onuldo.domain.user.QUser
import com.onuldo.port.inbound.feed.model.FeedQuery
import com.onuldo.port.inbound.feed.model.FeedSortType
import com.onuldo.port.inbound.feed.model.FeedType
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class FeedQueryRepository(
    private val queryFactory: JPAQueryFactory
) {
    private val record = QRecord.record
    private val user = QUser.user
    private val hobby = QHobby.hobby
    private val recordTag = QRecordTag.recordTag
    private val tag = QTag.tag

    fun findFeed(query: FeedQuery, followingIds: List<Long>): List<Record> {
        val whereConditions = mutableListOf<BooleanExpression>()

        // Profile feed (특정 사용자 피드)일 경우: 작성자 필터 + visibility 규칙
        val targetUserId = query.targetUserId
        if (targetUserId != null) {
            whereConditions.add(record.userId.eq(targetUserId))

            if (query.userId != targetUserId) {
                // 타인 프로필: 팔로우 여부에 따라 공개 범위 결정
                if (followingIds.contains(targetUserId)) {
                    whereConditions.add(record.visibility.`in`(RecordVisibility.PUBLIC, RecordVisibility.FOLLOWERS))
                } else {
                    whereConditions.add(record.visibility.eq(RecordVisibility.PUBLIC))
                }
            }
        } else {
            // Visibility condition
            when (query.feedType) {
                FeedType.ALL -> {
                    // Public records or own records
                    whereConditions.add(
                        record.visibility.eq(RecordVisibility.PUBLIC)
                            .or(record.userId.eq(query.userId))
                    )
                }
                FeedType.FOLLOWING -> {
                    // Following users' records (public + followers visibility)
                    if (followingIds.isEmpty()) {
                        // No following users, return own records only
                        whereConditions.add(record.userId.eq(query.userId))
                    } else {
                        whereConditions.add(
                            record.userId.`in`(followingIds + query.userId)
                                .and(
                                    record.visibility.eq(RecordVisibility.PUBLIC)
                                        .or(record.visibility.eq(RecordVisibility.FOLLOWERS))
                                        .or(record.userId.eq(query.userId))
                                )
                        )
                    }
                }
            }
        }

        // Hobby filter
        query.hobbyId?.let {
            whereConditions.add(record.hobbyId.eq(it))
        }

        // Tag filter (requires join)
        val tagFilterCondition = query.tagName?.let { tagName ->
            val tagIds = queryFactory
                .select(tag.id)
                .from(tag)
                .where(tag.name.eq(tagName.lowercase()))
                .fetch()

            if (tagIds.isNotEmpty()) {
                val recordIdsWithTag = queryFactory
                    .select(recordTag.recordId)
                    .from(recordTag)
                    .where(recordTag.tagId.`in`(tagIds))
                    .fetch()

                if (recordIdsWithTag.isNotEmpty()) {
                    record.id.`in`(recordIdsWithTag)
                } else {
                    record.id.eq(-1L) // No matching records
                }
            } else {
                record.id.eq(-1L) // Tag not found
            }
        }

        tagFilterCondition?.let { whereConditions.add(it) }

        // Build query
        val jpaQuery = queryFactory
            .selectFrom(record)
            .where(*whereConditions.toTypedArray())
            .orderBy(getOrderSpecifier(query.sortType))
            .offset((query.page * query.size).toLong())
            .limit(query.size.toLong())

        return jpaQuery.fetch()
    }

    private fun getOrderSpecifier(sortType: FeedSortType): OrderSpecifier<*> {
        return when (sortType) {
            FeedSortType.LATEST -> record.createdAt.desc()
            FeedSortType.POPULAR -> record.createdAt.desc() // TODO: Add reaction + comment count
        }
    }
}
