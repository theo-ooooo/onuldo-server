package com.onuldo.application.user

import com.onuldo.common.exception.CustomException
import com.onuldo.common.exception.ErrorCode
import com.onuldo.domain.user.User
import com.onuldo.port.inbound.user.model.RegisterUserCommand
import com.onuldo.port.inbound.user.usecase.RegisterUserUseCase
import com.onuldo.port.outbound.user.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RegisterUserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : RegisterUserUseCase {

    @Transactional
    override fun register(command: RegisterUserCommand): Long {
        if (userRepository.existsByEmail(command.email)) {
            throw CustomException(
                errorCode = ErrorCode.USER_EMAIL_DUPLICATE,
                details = mapOf("email" to command.email)
            )
        }

        if (userRepository.existsByNickname(command.nickname)) {
            throw CustomException(
                errorCode = ErrorCode.USER_NICKNAME_DUPLICATE,
                details = mapOf("nickname" to command.nickname)
            )
        }

        val encodedPassword = passwordEncoder.encode(command.rawPassword)

        val user = User(
            email = command.email,
            password = encodedPassword,
            nickname = command.nickname,
            profileImageUrl = command.profileImageUrl,
            bio = command.bio
        )

        val saved = userRepository.save(user)
        return saved.id
            ?: throw CustomException(ErrorCode.COMMON_INTERNAL_SERVER_ERROR, "사용자 ID 생성에 실패했습니다.")
    }
}


