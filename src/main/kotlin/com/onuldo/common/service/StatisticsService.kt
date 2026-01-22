package com.onuldo.common.service

import com.onuldo.port.outbound.record.RecordRepository
import com.onuldo.port.outbound.userhobby.UserHobbyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import org.springframework.stereotype.Service
import java.time.LocalDate

/**
 * 통계 서비스
 * 코루틴을 사용하여 여러 통계를 병렬로 계산합니다.
 */
@Service
class StatisticsService(
    private val recordRepository: RecordRepository,
    private val userHobbyRepository: UserHobbyRepository,
    private val coroutineScope: CoroutineScope
) {

    /**
     * 사용자의 통계를 병렬로 계산합니다.
     */
    suspend fun calculateUserStatistics(userId: Long): UserStatistics {
        return coroutineScope.async {
            // 여러 통계를 병렬로 계산
            val dailyStats = async { calculateDailyStatistics(userId) }
            val weeklyStats = async { calculateWeeklyStatistics(userId) }
            val monthlyStats = async { calculateMonthlyStatistics(userId) }
            val hobbyStats = async { calculateHobbyStatistics(userId) }

            // 모든 결과를 기다림
            UserStatistics(
                daily = dailyStats.await(),
                weekly = weeklyStats.await(),
                monthly = monthlyStats.await(),
                hobbyStats = hobbyStats.await()
            )
        }.await()
    }

    private suspend fun calculateDailyStatistics(userId: Long): DailyStatistics {
        val today = LocalDate.now()
        val records = recordRepository.findByUserIdAndActivityDate(userId, today)
        val totalDuration = records.sumOf { it.durationSeconds }
        return DailyStatistics(
            date = today,
            recordCount = records.size,
            totalDurationSeconds = totalDuration
        )
    }

    private suspend fun calculateWeeklyStatistics(userId: Long): WeeklyStatistics {
        val startDate = LocalDate.now().minusDays(7)
        val records = recordRepository.findByUserIdAndActivityDateBetween(userId, startDate, LocalDate.now())
        val totalDuration = records.sumOf { it.durationSeconds }
        return WeeklyStatistics(
            startDate = startDate,
            endDate = LocalDate.now(),
            recordCount = records.size,
            totalDurationSeconds = totalDuration
        )
    }

    private suspend fun calculateMonthlyStatistics(userId: Long): MonthlyStatistics {
        val startDate = LocalDate.now().minusMonths(1)
        val records = recordRepository.findByUserIdAndActivityDateBetween(userId, startDate, LocalDate.now())
        val totalDuration = records.sumOf { it.durationSeconds }
        return MonthlyStatistics(
            month = LocalDate.now().month,
            recordCount = records.size,
            totalDurationSeconds = totalDuration
        )
    }

    private suspend fun calculateHobbyStatistics(userId: Long): List<HobbyStatistics> {
        val userHobbies = userHobbyRepository.findByUserId(userId)
        return userHobbies.map { userHobby ->
            HobbyStatistics(
                hobbyId = userHobby.hobbyId,
                totalDurationSeconds = userHobby.totalDurationSeconds,
                recordCount = userHobby.recordCount
            )
        }
    }
}

// 통계 데이터 클래스들
data class UserStatistics(
    val daily: DailyStatistics,
    val weekly: WeeklyStatistics,
    val monthly: MonthlyStatistics,
    val hobbyStats: List<HobbyStatistics>
)

data class DailyStatistics(
    val date: LocalDate,
    val recordCount: Int,
    val totalDurationSeconds: Int
)

data class WeeklyStatistics(
    val startDate: LocalDate,
    val endDate: LocalDate,
    val recordCount: Int,
    val totalDurationSeconds: Int
)

data class MonthlyStatistics(
    val month: java.time.Month,
    val recordCount: Int,
    val totalDurationSeconds: Int
)

data class HobbyStatistics(
    val hobbyId: Long,
    val totalDurationSeconds: Int,
    val recordCount: Int
)

