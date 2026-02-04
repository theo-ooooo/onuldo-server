package com.onuldo.port.outbound.record

import com.onuldo.domain.record.RecordImage

interface RecordImageRepository {
    fun save(recordImage: RecordImage): RecordImage
    fun findById(id: Long): RecordImage?
    fun findByRecordId(recordId: Long): List<RecordImage>
    fun deleteById(id: Long)
    fun deleteByRecordId(recordId: Long)
}


