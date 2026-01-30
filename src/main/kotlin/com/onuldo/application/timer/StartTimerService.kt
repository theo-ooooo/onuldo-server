package com.onuldo.application.timer

import com.onuldo.common.exception.CustomException
import com.onuldo.common.exception.ErrorCode
import com.onuldo.common.exception.ResourceNotFoundException
import com.onuldo.domain.timer.Timer
import com.onuldo.port.inbound.timer.model.StartTimerCommand
import com.onuldo.port.inbound.timer.model.TimerResponse
import com.onuldo.port.inbound.timer.usecase.StartTimerUseCase
import com.onuldo.port.outbound.hobby.HobbyRepository
import com.onuldo.port.outbound.timer.TimerRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class StartTimerService(
    private val timerRepository: TimerRepository,
    private val hobbyRepository: HobbyRepository
) : StartTimerUseCase {

    @Transactional
    override fun execute(command: StartTimerCommand): TimerResponse {
        // Check if hobby exists
        hobbyRepository.findById(command.hobbyId)
            ?: throw ResourceNotFoundException("취미", command.hobbyId)

        // Check if user already has an active timer
        val activeTimer = timerRepository.findActiveTimerByUserId(command.userId)
        if (activeTimer != null) {
            throw CustomException(ErrorCode.TIMER_ALREADY_STARTED)
        }

        val timer = Timer(
            userId = command.userId,
            hobbyId = command.hobbyId,
            startTime = LocalDateTime.now()
        )

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
