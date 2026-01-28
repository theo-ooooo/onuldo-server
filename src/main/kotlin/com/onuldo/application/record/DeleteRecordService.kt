package com.onuldo.application.record

import com.onuldo.common.exception.ForbiddenException
import com.onuldo.common.exception.ResourceNotFoundException
import com.onuldo.port.inbound.record.usecase.DeleteRecordUseCase
import com.onuldo.port.outbound.record.RecordRepository
import com.onuldo.port.outbound.tag.RecordTagRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DeleteRecordService(
    private val recordRepository: RecordRepository,
    private val recordTagRepository: RecordTagRepository
) : DeleteRecordUseCase {

    @Transactional
    override fun execute(userId: Long, recordId: Long) {
        val record = recordRepository.findById(recordId)
            ?: throw ResourceNotFoundException("기록", recordId)

        if (record.userId != userId) {
            throw ForbiddenException("이 기록을 삭제할 권한이 없습니다.")
        }

        recordTagRepository.deleteByRecordId(recordId)
        recordRepository.deleteById(recordId)
    }
}
