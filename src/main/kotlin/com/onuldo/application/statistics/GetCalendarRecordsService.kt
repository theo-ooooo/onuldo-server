package com.onuldo.application.statistics

import com.onuldo.port.inbound.statistics.model.CalendarRecordResponse
import com.onuldo.port.inbound.statistics.usecase.GetCalendarRecordsUseCase
import com.onuldo.port.outbound.record.RecordRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.YearMonth

@Service
class GetCalendarRecordsService(
    private val recordRepository: RecordRepository
) : GetCalendarRecordsUseCase {

    @Transactional(readOnly = true)
    override fun execute(userId: Long, yearMonth: YearMonth): List<CalendarRecordResponse> {
        val startDate = yearMonth.atDay(1)
        val endDate = yearMonth.atEndOfMonth()
        
        val records = recordRepository.findByUserIdAndActivityDateBetween(userId, startDate, endDate)
        val recordsByDate = records.groupBy { it.activityDate }
        
        val allDates = startDate.datesUntil(endDate.plusDays(1)).toList()
        
        return allDates.map { date ->
            val dateRecords = recordsByDate[date] ?: emptyList()
            CalendarRecordResponse(
                date = date,
                hasRecord = dateRecords.isNotEmpty(),
                recordCount = dateRecords.size,
                totalDurationSeconds = dateRecords.sumOf { it.durationSeconds }
            )
        }
    }
}

