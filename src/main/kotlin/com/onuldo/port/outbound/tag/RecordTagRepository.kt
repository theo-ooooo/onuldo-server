package com.onuldo.port.outbound.tag

import com.onuldo.domain.tag.RecordTag

interface RecordTagRepository {
    fun save(recordTag: RecordTag): RecordTag
    fun saveAll(recordTags: List<RecordTag>): List<RecordTag>
    fun findByRecordId(recordId: Long): List<RecordTag>
    fun deleteByRecordId(recordId: Long)
}
