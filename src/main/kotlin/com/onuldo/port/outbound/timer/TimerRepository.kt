package com.onuldo.port.outbound.timer

import com.onuldo.domain.timer.Timer
import com.onuldo.domain.timer.TimerStatus

interface TimerRepository {
    fun save(timer: Timer): Timer
    fun findById(id: Long): Timer?
    fun findByUserId(userId: Long): List<Timer>
    fun findByUserIdAndStatus(userId: Long, status: TimerStatus): Timer?
    fun findActiveTimerByUserId(userId: Long): Timer?
}
