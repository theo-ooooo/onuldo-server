package com.onuldo.application.record

import com.onuldo.common.exception.ForbiddenException
import com.onuldo.common.exception.ResourceNotFoundException
import com.onuldo.domain.record.Record
import com.onuldo.domain.record.RecordVisibility
import com.onuldo.port.inbound.record.model.RecordResponse
import com.onuldo.port.inbound.record.usecase.GetRecordUseCase
import com.onuldo.port.outbound.record.RecordRepository
import com.onuldo.port.outbound.tag.RecordTagRepository
import com.onuldo.port.outbound.tag.TagRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetRecordService(
    private val recordRepository: RecordRepository,
    private val recordTagRepository: RecordTagRepository,
    private val tagRepository: TagRepository
) : GetRecordUseCase {

    @Transactional(readOnly = true)
    override fun execute(userId: Long, recordId: Long): RecordResponse {
        val record = recordRepository.findById(recordId)
            ?: throw ResourceNotFoundException("기록", recordId)

        // Check access permission
        if (record.userId != userId && record.visibility == RecordVisibility.PRIVATE) {
            throw ForbiddenException("이 기록에 접근할 권한이 없습니다.")
        }

        return toResponse(record)
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
