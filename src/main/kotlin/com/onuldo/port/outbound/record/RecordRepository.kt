package com.onuldo.port.outbound.record

import com.onuldo.domain.record.Record
import java.time.LocalDate

interface RecordRepository {
    fun save(record: Record): Record
    fun findById(id: Long): Record?
    fun findByUserId(userId: Long): List<Record>
    fun findByUserIdAndHobbyId(userId: Long, hobbyId: Long): List<Record>
    fun findByUserIdAndActivityDate(userId: Long, date: LocalDate): List<Record>
    fun findByUserIdAndActivityDateBetween(userId: Long, startDate: LocalDate, endDate: LocalDate): List<Record>
    fun deleteById(id: Long)
}

