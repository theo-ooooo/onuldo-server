package com.onuldo.application.timer

import com.onuldo.domain.timer.Timer
import com.onuldo.port.inbound.timer.model.TimerResponse
import com.onuldo.port.inbound.timer.usecase.GetCurrentTimerUseCase
import com.onuldo.port.outbound.timer.TimerRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetCurrentTimerService(
    private val timerRepository: TimerRepository
) : GetCurrentTimerUseCase {

    @Transactional(readOnly = true)
    override fun execute(userId: Long): TimerResponse? {
        val timer = timerRepository.findActiveTimerByUserId(userId)
        return timer?.let { toResponse(it) }
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
