package com.onuldo.application.user

import com.onuldo.common.exception.CustomException
import com.onuldo.common.exception.ErrorCode
import com.onuldo.common.exception.ResourceNotFoundException
import com.onuldo.port.inbound.user.usecase.UpdateFcmTokenUseCase
import com.onuldo.port.outbound.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UpdateFcmTokenService(
    private val userRepository: UserRepository
) : UpdateFcmTokenUseCase {

    @Transactional
    override fun execute(userId: Long, fcmToken: String?) {
        val user = userRepository.findById(userId)
            ?: throw ResourceNotFoundException("사용자", userId, ErrorCode.USER_NOT_FOUND)

        user.updateFcmToken(fcmToken)
    }
}


