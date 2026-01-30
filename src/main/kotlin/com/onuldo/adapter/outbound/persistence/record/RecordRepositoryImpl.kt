package com.onuldo.adapter.outbound.persistence.record

import com.onuldo.domain.record.Record
import com.onuldo.port.outbound.record.RecordRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class RecordRepositoryImpl(
    private val recordJpaRepository: RecordJpaRepository
) : RecordRepository {

    override fun save(record: Record): Record {
        return recordJpaRepository.save(record)
    }

    override fun findById(id: Long): Record? {
        return recordJpaRepository.findByIdOrNull(id)
    }

    override fun findByUserId(userId: Long): List<Record> {
        return recordJpaRepository.findByUserId(userId)
    }

    override fun findByUserIdAndHobbyId(userId: Long, hobbyId: Long): List<Record> {
        return recordJpaRepository.findByUserIdAndHobbyId(userId, hobbyId)
    }

    override fun findByUserIdAndActivityDate(userId: Long, date: LocalDate): List<Record> {
        return recordJpaRepository.findByUserIdAndActivityDate(userId, date)
    }

    override fun findByUserIdAndActivityDateBetween(userId: Long, startDate: LocalDate, endDate: LocalDate): List<Record> {
        return recordJpaRepository.findByUserIdAndActivityDateBetween(userId, startDate, endDate)
    }

    override fun deleteById(id: Long) {
        recordJpaRepository.deleteById(id)
    }
}
