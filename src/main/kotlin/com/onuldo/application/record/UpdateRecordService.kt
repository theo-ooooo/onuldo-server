package com.onuldo.application.record

import com.onuldo.common.exception.ForbiddenException
import com.onuldo.common.exception.ResourceNotFoundException
import com.onuldo.domain.record.Record
import com.onuldo.port.inbound.record.model.RecordResponse
import com.onuldo.port.inbound.record.model.UpdateRecordCommand
import com.onuldo.port.inbound.record.usecase.UpdateRecordUseCase
import com.onuldo.port.outbound.record.RecordRepository
import com.onuldo.port.outbound.tag.RecordTagRepository
import com.onuldo.port.outbound.tag.TagRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UpdateRecordService(
    private val recordRepository: RecordRepository,
    private val recordTagRepository: RecordTagRepository,
    private val tagRepository: TagRepository
) : UpdateRecordUseCase {

    @Transactional
    override fun execute(command: UpdateRecordCommand): RecordResponse {
        val record = recordRepository.findById(command.recordId)
            ?: throw ResourceNotFoundException("기록", command.recordId)

        if (record.userId != command.userId) {
            throw ForbiddenException("이 기록을 수정할 권한이 없습니다.")
        }

        command.memo?.let { record.updateMemo(it) }
        command.visibility?.let { record.changeVisibility(it) }

        val savedRecord = recordRepository.save(record)

        return toResponse(savedRecord)
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
