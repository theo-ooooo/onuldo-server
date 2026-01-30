package com.onuldo.application.timer

import com.onuldo.common.exception.CustomException
import com.onuldo.common.exception.ErrorCode
import com.onuldo.common.exception.ResourceNotFoundException
import com.onuldo.domain.timer.Timer
import com.onuldo.domain.timer.TimerStatus
import com.onuldo.port.inbound.timer.model.TimerResponse
import com.onuldo.port.inbound.timer.usecase.ResumeTimerUseCase
import com.onuldo.port.outbound.timer.TimerRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ResumeTimerService(
    private val timerRepository: TimerRepository
) : ResumeTimerUseCase {

    @Transactional
    override fun execute(userId: Long): TimerResponse {
        val timer = timerRepository.findByUserIdAndStatus(userId, TimerStatus.PAUSED)
            ?: throw ResourceNotFoundException("일시정지된 타이머")

        timer.resume()
        val savedTimer = timerRepository.save(timer)

        return toResponse(savedTimer)
    }

    private fun toResponse(timer: Timer): TimerResponse {
        return TimerResponse(
            id = timer.id ?: throw CustomException(ErrorCode.COMMON_INTERNAL_SERVER_ERROR, "타이머 ID가 없습니다."),
            hobbyId = timer.hobbyId,
            startTime = timer.startTime,
            endTime = timer.endTime,
            durationSeconds = timer.getCurrentDuration().toInt(),
            status = timer.status
        )
    }
}
