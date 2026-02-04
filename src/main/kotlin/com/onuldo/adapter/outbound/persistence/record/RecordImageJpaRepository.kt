package com.onuldo.adapter.outbound.persistence.record

import com.onuldo.domain.record.RecordImage
import org.springframework.data.jpa.repository.JpaRepository

interface RecordImageJpaRepository : JpaRepository<RecordImage, Long> {
    fun findByRecordId(recordId: Long): List<RecordImage>
    fun deleteByRecordId(recordId: Long)
}


