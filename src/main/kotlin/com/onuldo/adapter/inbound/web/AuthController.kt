package com.onuldo.adapter.inbound.web

import com.onuldo.common.dto.ApiResponse
import com.onuldo.port.inbound.user.model.LoginResult
import com.onuldo.port.inbound.user.model.LoginUserCommand
import com.onuldo.port.inbound.user.model.RegisterUserCommand
import com.onuldo.port.inbound.user.usecase.LoginUserUseCase
import com.onuldo.port.inbound.user.usecase.RefreshTokenUseCase
import com.onuldo.port.inbound.user.usecase.RegisterUserUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Auth", description = "인증 API")
@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val registerUserUseCase: RegisterUserUseCase,
    private val loginUserUseCase: LoginUserUseCase,
    private val refreshTokenUseCase: RefreshTokenUseCase
) {

    @Operation(summary = "회원가입", description = "이메일/비밀번호/닉네임으로 회원가입을 수행합니다.")
    @PostMapping("/signup")
    fun signup(@Validated @RequestBody request: SignUpRequest): ApiResponse<SignUpResponse> {
        val userId = registerUserUseCase.register(
            RegisterUserCommand(
                email = request.email,
                rawPassword = request.password,
                nickname = request.nickname,
                profileImageUrl = request.profileImageUrl,
                bio = request.bio
            )
        )

        return ApiResponse.success(
            SignUpResponse(
                userId = userId,
                email = request.email,
                nickname = request.nickname
            ),
            message = "회원가입이 완료되었습니다."
        )
    }

    @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인을 수행합니다.")
    @PostMapping("/login")
    fun login(@Validated @RequestBody request: LoginRequest): ApiResponse<LoginResult> {
        val result = loginUserUseCase.login(
            LoginUserCommand(
                email = request.email,
                rawPassword = request.password
            )
        )
        return ApiResponse.success(result, message = "로그인에 성공했습니다.")
    }

    @Operation(summary = "토큰 재발급", description = "리프레시 토큰으로 액세스 토큰을 재발급합니다.")
    @PostMapping("/refresh")
    fun refresh(@Validated @RequestBody request: RefreshTokenRequest): ApiResponse<LoginResult> {
        val result = refreshTokenUseCase.refresh(request.refreshToken)
        return ApiResponse.success(result, message = "토큰이 재발급되었습니다.")
    }
}

data class SignUpRequest(
    @field:Email
    @field:NotBlank
    val email: String,

    @field:NotBlank
    @field:Size(min = 8, max = 100)
    val password: String,

    @field:NotBlank
    @field:Size(min = 2, max = 50)
    val nickname: String,

    val profileImageUrl: String? = null,
    val bio: String? = null
)

data class SignUpResponse(
    val userId: Long,
    val email: String,
    val nickname: String
)

data class LoginRequest(
    @field:Email
    @field:NotBlank
    val email: String,

    @field:NotBlank
    val password: String
)

data class RefreshTokenRequest(
    @field:NotBlank
    val refreshToken: String
)



