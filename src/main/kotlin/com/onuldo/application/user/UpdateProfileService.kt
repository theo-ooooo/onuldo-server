package com.onuldo.application.user

import com.onuldo.common.exception.CustomException
import com.onuldo.common.exception.ErrorCode
import com.onuldo.common.exception.ResourceNotFoundException
import com.onuldo.port.inbound.user.model.UpdateProfileCommand
import com.onuldo.port.inbound.user.usecase.UpdateProfileUseCase
import com.onuldo.port.outbound.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UpdateProfileService(
    private val userRepository: UserRepository
) : UpdateProfileUseCase {

    @Transactional
    override fun execute(command: UpdateProfileCommand) {
        val user = userRepository.findById(command.userId)
            ?: throw ResourceNotFoundException("사용자", command.userId, ErrorCode.USER_NOT_FOUND)

        // 닉네임 변경
        if (command.nickname != null && command.nickname != user.nickname) {
            // 닉네임 중복 체크
            if (userRepository.existsByNickname(command.nickname)) {
                throw CustomException(
                    errorCode = ErrorCode.USER_NICKNAME_DUPLICATE,
                    message = "이미 사용 중인 닉네임입니다."
                )
            }
            user.changeNickname(command.nickname)
        }

        // 소개 변경
        if (command.bio != null) {
            user.updateProfile(user.profileImageUrl, command.bio)
        }

        userRepository.save(user)
    }
}

