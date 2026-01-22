package com.onuldo.domain.timer

import com.onuldo.domain.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Index
import jakarta.persistence.Table
import java.time.LocalDateTime

/**
 * 타이머 엔티티
 */
@Entity
@Table(
    name = "timers",
    indexes = [
        jakarta.persistence.Index(name = "idx_timer_user", columnList = "user_id"),
        jakarta.persistence.Index(name = "idx_timer_status", columnList = "status")
    ]
)
class Timer(
    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Column(name = "hobby_id", nullable = false)
    val hobbyId: Long,

    @Column(name = "start_time", nullable = false)
    var startTime: LocalDateTime,

    @Column(name = "end_time")
    var endTime: LocalDateTime? = null,

    @Column(name = "duration_seconds", nullable = false)
    var durationSeconds: Int = 0,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var status: TimerStatus = TimerStatus.RUNNING
) : BaseEntity() {

    fun pause() {
        if (status != TimerStatus.RUNNING) {
            throw IllegalStateException("실행 중인 타이머만 일시정지할 수 있습니다.")
        }
        status = TimerStatus.PAUSED
        updateDuration()
    }

    fun resume() {
        if (status != TimerStatus.PAUSED) {
            throw IllegalStateException("일시정지된 타이머만 재개할 수 있습니다.")
        }
        status = TimerStatus.RUNNING
        startTime = LocalDateTime.now()
    }

    fun stop() {
        if (status == TimerStatus.STOPPED) {
            throw IllegalStateException("이미 종료된 타이머입니다.")
        }
        status = TimerStatus.STOPPED
        endTime = LocalDateTime.now()
        updateDuration()
    }

    private fun updateDuration() {
        if (endTime != null) {
            durationSeconds = java.time.Duration.between(startTime, endTime).seconds.toInt()
        } else {
            val now = LocalDateTime.now()
            durationSeconds = java.time.Duration.between(startTime, now).seconds.toInt()
        }
    }

    fun getCurrentDuration(): Int {
        return if (status == TimerStatus.RUNNING) {
            val now = LocalDateTime.now()
            java.time.Duration.between(startTime, now).seconds.toInt()
        } else {
            durationSeconds
        }
    }
}

