package com.onuldo.port.inbound.user.usecase

import com.onuldo.port.inbound.user.model.LoginResult

/**
 * 리프레시 토큰으로 액세스 토큰을 재발급하는 유스케이스
 */
interface RefreshTokenUseCase {

    fun refresh(refreshToken: String): LoginResult
}


