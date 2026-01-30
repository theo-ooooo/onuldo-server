package com.onuldo.domain.timer

import com.onuldo.common.exception.CustomException
import com.onuldo.common.exception.ErrorCode
import com.onuldo.domain.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Index
import jakarta.persistence.Table
import java.time.Duration
import java.time.LocalDateTime

/**
 * 타이머 엔티티
 */
@Entity
@Table(
    name = "timers",
    indexes = [
        Index(name = "idx_timer_user_id", columnList = "user_id"),
        Index(name = "idx_timer_user_status", columnList = "user_id,status")
    ]
)
class Timer(
    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Column(name = "hobby_id", nullable = false)
    val hobbyId: Long,

    @Column(name = "start_time", nullable = false)
    val startTime: LocalDateTime,

    @Column(name = "end_time")
    var endTime: LocalDateTime? = null,

    @Column(name = "paused_at")
    var pausedAt: LocalDateTime? = null,

    @Column(name = "total_paused_duration_seconds", nullable = false)
    var totalPausedDurationSeconds: Long = 0,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var status: TimerStatus = TimerStatus.RUNNING
) : BaseEntity() {

    fun pause() {
        if (status != TimerStatus.RUNNING) {
            throw CustomException(ErrorCode.TIMER_NOT_STARTED, "일시정지할 수 없는 상태입니다: $status")
        }
        pausedAt = LocalDateTime.now()
        status = TimerStatus.PAUSED
    }

    fun resume() {
        if (status != TimerStatus.PAUSED) {
            throw CustomException(ErrorCode.TIMER_NOT_STARTED, "재개할 수 없는 상태입니다: $status")
        }
        val now = LocalDateTime.now()
        pausedAt?.let {
            totalPausedDurationSeconds += Duration.between(it, now).seconds
        }
        pausedAt = null
        status = TimerStatus.RUNNING
    }

    fun stop() {
        if (status == TimerStatus.STOPPED) {
            throw CustomException(ErrorCode.TIMER_NOT_STARTED, "이미 종료된 타이머입니다.")
        }
        val now = LocalDateTime.now()
        if (pausedAt != null) {
            totalPausedDurationSeconds += Duration.between(pausedAt, now).seconds
            pausedAt = null
        }
        endTime = now
        status = TimerStatus.STOPPED
    }

    fun getCurrentDuration(): Long {
        val now = if (status == TimerStatus.STOPPED && endTime != null) {
            endTime!!
        } else {
            LocalDateTime.now()
        }

        val baseDuration = Duration.between(startTime, now).seconds
        val currentPausedDuration = if (pausedAt != null) {
            Duration.between(pausedAt, now).seconds
        } else {
            0
        }

        return maxOf(0, baseDuration - totalPausedDurationSeconds - currentPausedDuration)
    }
}

