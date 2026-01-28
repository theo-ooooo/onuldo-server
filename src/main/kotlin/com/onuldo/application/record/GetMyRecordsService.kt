package com.onuldo.application.record

import com.onuldo.domain.record.Record
import com.onuldo.port.inbound.record.model.RecordResponse
import com.onuldo.port.inbound.record.usecase.GetMyRecordsUseCase
import com.onuldo.port.outbound.record.RecordRepository
import com.onuldo.port.outbound.tag.RecordTagRepository
import com.onuldo.port.outbound.tag.TagRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetMyRecordsService(
    private val recordRepository: RecordRepository,
    private val recordTagRepository: RecordTagRepository,
    private val tagRepository: TagRepository
) : GetMyRecordsUseCase {

    @Transactional(readOnly = true)
    override fun execute(userId: Long, page: Int, size: Int): List<RecordResponse> {
        val records = recordRepository.findByUserIdPaged(userId, page, size)
        return records.map { toResponse(it) }
    }

    private fun toResponse(record: Record): RecordResponse {
        val recordTags = recordTagRepository.findByRecordId(record.id!!)
        val tagIds = recordTags.map { it.tagId }
        val tags = if (tagIds.isNotEmpty()) tagRepository.findByIds(tagIds) else emptyList()

        return RecordResponse(
            id = record.id!!,
            userId = record.userId,
            hobbyId = record.hobbyId,
            timerId = record.timerId,
            durationSeconds = record.durationSeconds,
            memo = record.memo,
            visibility = record.visibility,
            activityDate = record.activityDate,
            tags = tags.map { it.name },
            createdAt = record.createdAt!!
        )
    }
}
