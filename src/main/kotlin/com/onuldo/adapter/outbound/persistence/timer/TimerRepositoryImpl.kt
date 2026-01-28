package com.onuldo.adapter.outbound.persistence.timer

import com.onuldo.domain.timer.Timer
import com.onuldo.domain.timer.TimerStatus
import com.onuldo.port.outbound.timer.TimerRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class TimerRepositoryImpl(
    private val timerJpaRepository: TimerJpaRepository
) : TimerRepository {

    override fun save(timer: Timer): Timer {
        return timerJpaRepository.save(timer)
    }

    override fun findById(id: Long): Timer? {
        return timerJpaRepository.findByIdOrNull(id)
    }

    override fun findByUserId(userId: Long): List<Timer> {
        return timerJpaRepository.findByUserId(userId)
    }

    override fun findByUserIdAndStatus(userId: Long, status: TimerStatus): Timer? {
        return timerJpaRepository.findByUserIdAndStatus(userId, status)
    }

    override fun findActiveTimerByUserId(userId: Long): Timer? {
        return timerJpaRepository.findActiveTimerByUserId(userId)
    }
}
