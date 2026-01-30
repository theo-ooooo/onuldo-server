package com.onuldo.application.statistics

import com.onuldo.port.inbound.statistics.model.DailyStatisticsResponse
import com.onuldo.port.inbound.statistics.usecase.GetDailyStatisticsUseCase
import com.onuldo.port.outbound.record.RecordRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class GetDailyStatisticsService(
    private val recordRepository: RecordRepository
) : GetDailyStatisticsUseCase {

    @Transactional(readOnly = true)
    override fun execute(userId: Long, startDate: LocalDate, endDate: LocalDate): List<DailyStatisticsResponse> {
        val records = recordRepository.findByUserIdAndActivityDateBetween(userId, startDate, endDate)
        
        val statisticsByDate = records.groupBy { it.activityDate }
            .mapValues { (_, records) ->
                DailyStatisticsResponse(
                    date = records.first().activityDate,
                    totalDurationSeconds = records.sumOf { it.durationSeconds },
                    recordCount = records.size
                )
            }
        
        // Fill in missing dates with zero statistics
        val allDates = startDate.datesUntil(endDate.plusDays(1)).toList()
        return allDates.map { date ->
            statisticsByDate[date] ?: DailyStatisticsResponse(
                date = date,
                totalDurationSeconds = 0,
                recordCount = 0
            )
        }
    }
}

