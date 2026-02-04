package com.onuldo.adapter.outbound.persistence.record

import com.onuldo.domain.record.RecordImage
import com.onuldo.port.outbound.record.RecordImageRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class RecordImageRepositoryImpl(
    private val recordImageJpaRepository: RecordImageJpaRepository
) : RecordImageRepository {

    override fun save(recordImage: RecordImage): RecordImage {
        return recordImageJpaRepository.save(recordImage)
    }

    override fun findById(id: Long): RecordImage? {
        return recordImageJpaRepository.findByIdOrNull(id)
    }

    override fun findByRecordId(recordId: Long): List<RecordImage> {
        return recordImageJpaRepository.findByRecordId(recordId)
    }

    override fun deleteById(id: Long) {
        recordImageJpaRepository.deleteById(id)
    }

    override fun deleteByRecordId(recordId: Long) {
        recordImageJpaRepository.deleteByRecordId(recordId)
    }
}


