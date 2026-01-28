package com.onuldo.adapter.outbound.persistence.record

import com.onuldo.domain.record.Record
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface RecordJpaRepository : JpaRepository<Record, Long> {
    fun findByUserId(userId: Long): List<Record>
    fun findByUserId(userId: Long, pageable: Pageable): Page<Record>
    fun findByUserIdAndActivityDate(userId: Long, activityDate: LocalDate): List<Record>
    fun findByUserIdAndActivityDateBetween(userId: Long, startDate: LocalDate, endDate: LocalDate): List<Record>
}
