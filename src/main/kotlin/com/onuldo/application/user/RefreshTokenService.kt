package com.onuldo.application.user

import com.onuldo.common.exception.CustomException
import com.onuldo.common.exception.ErrorCode
import com.onuldo.common.security.JwtTokenProvider
import com.onuldo.port.inbound.user.model.LoginResult
import com.onuldo.port.inbound.user.usecase.RefreshTokenUseCase
import com.onuldo.port.outbound.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RefreshTokenService(
    private val jwtTokenProvider: JwtTokenProvider,
    private val userRepository: UserRepository
) : RefreshTokenUseCase {

    @Transactional(readOnly = true)
    override fun refresh(refreshToken: String): LoginResult {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw CustomException(
                errorCode = ErrorCode.AUTH_TOKEN_INVALID,
                message = "유효하지 않은 리프레시 토큰입니다."
            )
        }

        // 리프레시 토큰은 인증 컨텍스트에 사용하지 않으므로 type 검사만 ACCESS가 아님을 보장
        if (jwtTokenProvider.isAccessToken(refreshToken)) {
            throw CustomException(
                errorCode = ErrorCode.AUTH_TOKEN_INVALID,
                message = "리프레시 토큰이 아닙니다."
            )
        }

        val userId = jwtTokenProvider.getUserId(refreshToken)
        val email = jwtTokenProvider.getEmail(refreshToken)

        val user = userRepository.findById(userId)
            ?: throw CustomException(
                errorCode = ErrorCode.USER_NOT_FOUND,
                message = "사용자를 찾을 수 없습니다."
            )

        if (user.email != email) {
            throw CustomException(
                errorCode = ErrorCode.AUTH_TOKEN_INVALID,
                message = "토큰 정보가 사용자와 일치하지 않습니다."
            )
        }

        val newAccessToken = jwtTokenProvider.generateAccessToken(userId, user.email)
        val newRefreshToken = jwtTokenProvider.generateRefreshToken(userId, user.email)

        return LoginResult(
            userId = userId,
            email = user.email,
            nickname = user.nickname,
            accessToken = newAccessToken,
            refreshToken = newRefreshToken
        )
    }
}


