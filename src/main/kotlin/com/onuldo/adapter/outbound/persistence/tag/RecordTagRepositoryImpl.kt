package com.onuldo.adapter.outbound.persistence.tag

import com.onuldo.domain.tag.RecordTag
import com.onuldo.port.outbound.tag.RecordTagRepository
import org.springframework.stereotype.Repository

@Repository
class RecordTagRepositoryImpl(
    private val recordTagJpaRepository: RecordTagJpaRepository
) : RecordTagRepository {

    override fun save(recordTag: RecordTag): RecordTag {
        return recordTagJpaRepository.save(recordTag)
    }

    override fun saveAll(recordTags: List<RecordTag>): List<RecordTag> {
        return recordTagJpaRepository.saveAll(recordTags)
    }

    override fun findByRecordId(recordId: Long): List<RecordTag> {
        return recordTagJpaRepository.findByRecordId(recordId)
    }

    override fun deleteByRecordId(recordId: Long) {
        recordTagJpaRepository.deleteByRecordId(recordId)
    }
}
