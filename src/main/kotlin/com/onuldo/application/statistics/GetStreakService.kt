package com.onuldo.application.statistics

import com.onuldo.port.inbound.statistics.model.StreakResponse
import com.onuldo.port.inbound.statistics.usecase.GetStreakUseCase
import com.onuldo.port.outbound.record.RecordRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class GetStreakService(
    private val recordRepository: RecordRepository
) : GetStreakUseCase {

    @Transactional(readOnly = true)
    override fun execute(userId: Long): StreakResponse {
        val records = recordRepository.findByUserId(userId)
        val recordDates = records.map { it.activityDate }.distinct().sortedDescending()
        
        if (recordDates.isEmpty()) {
            return StreakResponse(
                currentStreak = 0,
                longestStreak = 0,
                lastRecordDate = null
            )
        }
        
        val lastRecordDate = recordDates.first()
        val today = LocalDate.now()
        
        // Calculate current streak
        var currentStreak = 0
        var checkDate = today
        var recordIndex = 0
        
        while (recordIndex < recordDates.size) {
            val recordDate = recordDates[recordIndex]
            if (recordDate == checkDate || recordDate == checkDate.minusDays(1)) {
                if (recordDate == checkDate) {
                    currentStreak++
                    checkDate = checkDate.minusDays(1)
                } else {
                    // If there's a gap, streak is broken
                    break
                }
            } else if (recordDate < checkDate) {
                // Gap found, streak is broken
                break
            }
            recordIndex++
        }
        
        // If last record is not today or yesterday, streak is 0
        if (lastRecordDate < today.minusDays(1)) {
            currentStreak = 0
        }
        
        // Calculate longest streak
        var longestStreak = 0
        var tempStreak = 0
        var prevDate: LocalDate? = null
        
        for (date in recordDates.reversed()) {
            if (prevDate == null) {
                tempStreak = 1
            } else {
                val daysBetween = prevDate.toEpochDay() - date.toEpochDay()
                if (daysBetween == 1L) {
                    tempStreak++
                } else {
                    longestStreak = maxOf(longestStreak, tempStreak)
                    tempStreak = 1
                }
            }
            prevDate = date
        }
        longestStreak = maxOf(longestStreak, tempStreak)
        
        return StreakResponse(
            currentStreak = currentStreak,
            longestStreak = longestStreak,
            lastRecordDate = lastRecordDate
        )
    }
}

