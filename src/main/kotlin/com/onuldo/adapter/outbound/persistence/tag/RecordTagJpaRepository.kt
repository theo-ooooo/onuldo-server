package com.onuldo.adapter.outbound.persistence.tag

import com.onuldo.domain.tag.RecordTag
import org.springframework.data.jpa.repository.JpaRepository

interface RecordTagJpaRepository : JpaRepository<RecordTag, Long> {
    fun findByRecordId(recordId: Long): List<RecordTag>
    fun deleteByRecordId(recordId: Long)
}
