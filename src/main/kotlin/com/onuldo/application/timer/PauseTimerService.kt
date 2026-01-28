package com.onuldo.application.timer

import com.onuldo.common.exception.ResourceNotFoundException
import com.onuldo.domain.timer.Timer
import com.onuldo.port.inbound.timer.model.TimerResponse
import com.onuldo.port.inbound.timer.usecase.PauseTimerUseCase
import com.onuldo.port.outbound.timer.TimerRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PauseTimerService(
    private val timerRepository: TimerRepository
) : PauseTimerUseCase {

    @Transactional
    override fun execute(userId: Long): TimerResponse {
        val timer = timerRepository.findActiveTimerByUserId(userId)
            ?: throw ResourceNotFoundException("활성 타이머")

        timer.pause()
        val savedTimer = timerRepository.save(timer)

        return toResponse(savedTimer)
    }

    private fun toResponse(timer: Timer): TimerResponse {
        return TimerResponse(
            id = timer.id ?: throw IllegalStateException("타이머 ID가 없습니다."),
            hobbyId = timer.hobbyId,
            startTime = timer.startTime,
            endTime = timer.endTime,
            durationSeconds = timer.getCurrentDuration(),
            status = timer.status
        )
    }
}
