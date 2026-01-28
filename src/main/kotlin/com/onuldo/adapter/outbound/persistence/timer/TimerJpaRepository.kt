package com.onuldo.adapter.outbound.persistence.timer

import com.onuldo.domain.timer.Timer
import com.onuldo.domain.timer.TimerStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface TimerJpaRepository : JpaRepository<Timer, Long> {
    fun findByUserId(userId: Long): List<Timer>
    fun findByUserIdAndStatus(userId: Long, status: TimerStatus): Timer?

    @Query("SELECT t FROM Timer t WHERE t.userId = :userId AND t.status IN ('RUNNING', 'PAUSED')")
    fun findActiveTimerByUserId(userId: Long): Timer?
}
