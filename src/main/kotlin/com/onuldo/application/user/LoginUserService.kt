package com.onuldo.application.user

import com.onuldo.common.exception.CustomException
import com.onuldo.common.exception.ErrorCode
import com.onuldo.common.security.JwtTokenProvider
import com.onuldo.port.inbound.user.model.LoginResult
import com.onuldo.port.inbound.user.model.LoginUserCommand
import com.onuldo.port.inbound.user.usecase.LoginUserUseCase
import com.onuldo.port.outbound.user.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class LoginUserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider
) : LoginUserUseCase {

    @Transactional(readOnly = true)
    override fun login(command: LoginUserCommand): LoginResult {
        val user = userRepository.findByEmail(command.email)
            ?: throw CustomException(
                errorCode = ErrorCode.AUTH_UNAUTHORIZED,
                message = "이메일 또는 비밀번호가 올바르지 않습니다."
            )

        if (!passwordEncoder.matches(command.rawPassword, user.password)) {
            throw CustomException(
                errorCode = ErrorCode.AUTH_UNAUTHORIZED,
                message = "이메일 또는 비밀번호가 올바르지 않습니다."
            )
        }

        val userId = user.id
            ?: throw CustomException(ErrorCode.COMMON_INTERNAL_SERVER_ERROR, "사용자 ID가 없습니다.")

        val accessToken = jwtTokenProvider.generateAccessToken(userId, user.email)
        val refreshToken = jwtTokenProvider.generateRefreshToken(userId, user.email)

        return LoginResult(
            userId = userId,
            email = user.email,
            nickname = user.nickname,
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }



}


