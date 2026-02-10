package com.onuldo.application.user

import com.onuldo.common.exception.CustomException
import com.onuldo.common.exception.ErrorCode
import com.onuldo.common.exception.ResourceNotFoundException
import com.onuldo.port.inbound.user.model.ChangePasswordCommand
import com.onuldo.port.inbound.user.usecase.ChangePasswordUseCase
import com.onuldo.port.outbound.user.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ChangePasswordService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : ChangePasswordUseCase {

    @Transactional
    override fun execute(command: ChangePasswordCommand) {
        val user = userRepository.findById(command.userId)
            ?: throw ResourceNotFoundException("사용자", command.userId, ErrorCode.USER_NOT_FOUND)

        // 현재 비밀번호 검증
        val currentPassword = user.password
        if (currentPassword == null || !passwordEncoder.matches(command.currentPassword, currentPassword)) {
            throw CustomException(
                errorCode = ErrorCode.AUTH_UNAUTHORIZED,
                message = "현재 비밀번호가 올바르지 않습니다."
            )
        }

        // 새 비밀번호가 현재 비밀번호와 같은지 확인
        if (passwordEncoder.matches(command.newPassword, currentPassword)) {
            throw CustomException(
                errorCode = ErrorCode.COMMON_INVALID_INPUT,
                message = "새 비밀번호는 현재 비밀번호와 달라야 합니다."
            )
        }

        // 새 비밀번호 인코딩 및 변경
        val encodedPassword = passwordEncoder.encode(command.newPassword)!!
        user.changePassword(encodedPassword)
        userRepository.save(user)
    }
}

