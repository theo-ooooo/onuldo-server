package com.onuldo.application.record

import com.onuldo.common.exception.ResourceNotFoundException
import com.onuldo.domain.record.Record
import com.onuldo.domain.tag.RecordTag
import com.onuldo.domain.tag.Tag
import com.onuldo.port.inbound.record.model.CreateRecordCommand
import com.onuldo.port.inbound.record.model.RecordResponse
import com.onuldo.port.inbound.record.usecase.CreateRecordUseCase
import com.onuldo.port.outbound.hobby.HobbyRepository
import com.onuldo.port.outbound.record.RecordRepository
import com.onuldo.port.outbound.tag.RecordTagRepository
import com.onuldo.port.outbound.tag.TagRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CreateRecordService(
    private val recordRepository: RecordRepository,
    private val hobbyRepository: HobbyRepository,
    private val tagRepository: TagRepository,
    private val recordTagRepository: RecordTagRepository
) : CreateRecordUseCase {

    @Transactional
    override fun execute(command: CreateRecordCommand): RecordResponse {
        hobbyRepository.findById(command.hobbyId)
            ?: throw ResourceNotFoundException("취미", command.hobbyId)

        val record = Record(
            userId = command.userId,
            hobbyId = command.hobbyId,
            timerId = command.timerId,
            durationSeconds = command.durationSeconds,
            memo = command.memo,
            visibility = command.visibility,
            activityDate = command.activityDate
        )

        val savedRecord = recordRepository.save(record)
        val recordId = savedRecord.id ?: throw IllegalStateException("기록 ID가 없습니다.")

        val tagNames = processTags(command.tagNames, recordId)

        return RecordResponse(
            id = recordId,
            userId = savedRecord.userId,
            hobbyId = savedRecord.hobbyId,
            timerId = savedRecord.timerId,
            durationSeconds = savedRecord.durationSeconds,
            memo = savedRecord.memo,
            visibility = savedRecord.visibility,
            activityDate = savedRecord.activityDate,
            tags = tagNames,
            createdAt = savedRecord.createdAt!!
        )
    }

    private fun processTags(tagNames: List<String>, recordId: Long): List<String> {
        if (tagNames.isEmpty()) return emptyList()

        val normalizedNames = tagNames.map { it.trim().lowercase() }.distinct()
        val existingTags = tagRepository.findByNameIn(normalizedNames)
        val existingTagNames = existingTags.map { it.name }.toSet()

        val newTags = normalizedNames.filter { it !in existingTagNames }.map { name ->
            tagRepository.save(Tag(name = name))
        }

        val allTags = existingTags + newTags

        val recordTags = allTags.map { tag ->
            tag.incrementUsageCount()
            tagRepository.save(tag)
            RecordTag(recordId = recordId, tagId = tag.id!!)
        }

        recordTagRepository.saveAll(recordTags)

        return allTags.map { it.name }
    }
}
