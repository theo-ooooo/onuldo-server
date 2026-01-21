package com.onuldo.port.inbound.user.usecase

import com.onuldo.port.inbound.user.model.RegisterUserCommand

/**
 * 회원가입 유스케이스 (Inbound Port)
 *
 * 실제 요청/응답(JSON)은 어댑터(Web) 계층의 Request/Response DTO에서 처리하고,
 * 이 포트에서는 유스케이스 관점의 Command만 사용합니다.
 */
interface RegisterUserUseCase {

    fun register(command: RegisterUserCommand): Long
}


