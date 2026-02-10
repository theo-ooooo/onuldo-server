package com.onuldo.application.feed

import com.onuldo.adapter.outbound.persistence.feed.FeedQueryRepository
import com.onuldo.domain.record.Record
import com.onuldo.port.inbound.feed.model.FeedImageResponse
import com.onuldo.port.inbound.feed.model.FeedItemResponse
import com.onuldo.port.inbound.feed.model.FeedQuery
import com.onuldo.port.inbound.feed.model.FeedType
import com.onuldo.port.inbound.feed.usecase.GetFeedUseCase
import com.onuldo.port.outbound.follow.FollowRepository
import com.onuldo.port.outbound.hobby.HobbyRepository
import com.onuldo.port.outbound.reaction.ReactionRepository
import com.onuldo.port.outbound.record.RecordImageRepository
import com.onuldo.port.outbound.tag.RecordTagRepository
import com.onuldo.port.outbound.tag.TagRepository
import com.onuldo.port.outbound.user.UserRepository
import com.onuldo.common.config.S3Properties
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetFeedService(
    private val feedQueryRepository: FeedQueryRepository,
    private val followRepository: FollowRepository,
    private val userRepository: UserRepository,
    private val hobbyRepository: HobbyRepository,
    private val recordTagRepository: RecordTagRepository,
    private val tagRepository: TagRepository,
    private val reactionRepository: ReactionRepository,
    private val recordImageRepository: RecordImageRepository,
    private val s3Properties: S3Properties
) : GetFeedUseCase {

    @Transactional(readOnly = true)
    override fun execute(query: FeedQuery): List<FeedItemResponse> {
        val followingIds = if (query.feedType == FeedType.FOLLOWING) {
            followRepository.findFollowingIdsByFollowerId(query.userId)
        } else {
            emptyList()
        }

        val records = feedQueryRepository.findFeed(query, followingIds)

        return records.map { record -> toFeedItemResponse(record) }
    }

    private fun toFeedItemResponse(record: Record): FeedItemResponse {
        val user = userRepository.findById(record.userId)
        val hobby = hobbyRepository.findById(record.hobbyId)
        val recordTags = recordTagRepository.findByRecordId(record.id!!)
        val tagIds = recordTags.map { it.tagId }
        val tags = if (tagIds.isNotEmpty()) tagRepository.findByIds(tagIds) else emptyList()
        val reactionCounts = reactionRepository.countByRecordIdGroupByEmojiType(record.id!!)
        val images = recordImageRepository.findByRecordId(record.id!!).map { image ->
            FeedImageResponse(
                imageId = image.imageId,
                imageUrl = "${s3Properties.baseUrl}/${image.imageUrl}",
                width = image.width,
                height = image.height
            )
        }

        return FeedItemResponse(
            recordId = record.id!!,
            userId = record.userId,
            userNickname = user?.nickname ?: "Unknown",
            userProfileImageUrl = user?.profileImageUrl,
            hobbyId = record.hobbyId,
            hobbyName = hobby?.name ?: "Unknown",
            durationSeconds = record.durationSeconds,
            memo = record.memo,
            visibility = record.visibility,
            activityDate = record.activityDate,
            tags = tags.map { it.name },
            images = images,
            reactionCounts = reactionCounts,
            commentCount = 0,  // TODO: Add comment count
            createdAt = record.createdAt!!
        )
    }
}
