package com.onuldo.application.user

import com.onuldo.adapter.outbound.storage.S3FileStorage
import com.onuldo.common.exception.CustomException
import com.onuldo.common.exception.ErrorCode
import com.onuldo.common.exception.ResourceNotFoundException
import com.onuldo.port.inbound.user.model.ConfirmProfileImageUploadCommand
import com.onuldo.port.inbound.user.model.ProfileImageUploadResponse
import com.onuldo.port.inbound.user.usecase.ConfirmProfileImageUploadUseCase
import com.onuldo.port.outbound.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ConfirmProfileImageUploadService(
    private val userRepository: UserRepository,
    private val s3FileStorage: S3FileStorage
) : ConfirmProfileImageUploadUseCase {

    @Transactional
    override fun execute(command: ConfirmProfileImageUploadCommand): ProfileImageUploadResponse {
        // 사용자 존재 확인
        val user = userRepository.findById(command.userId)
            ?: throw ResourceNotFoundException("사용자", command.userId, ErrorCode.USER_NOT_FOUND)

        // S3에 파일 존재 확인
        if (!s3FileStorage.exists(command.imageKey)) {
            throw CustomException(
                errorCode = ErrorCode.COMMON_INVALID_INPUT,
                message = "업로드된 이미지를 찾을 수 없습니다."
            )
        }

        // 기존 프로필 이미지 삭제 (있는 경우)
        val existingProfileImageUrl = user.profileImageUrl
        if (existingProfileImageUrl != null) {
            try {
                s3FileStorage.deleteFile(existingProfileImageUrl)
            } catch (e: Exception) {
                // 삭제 실패는 로그만 남기고 계속 진행
            }
        }

        // Presigned 다운로드 URL 생성 (7일 유효)
        val imageUrl = s3FileStorage.generatePresignedDownloadUrl(command.imageKey, 60 * 24 * 7)

        // 사용자 프로필 이미지 URL 업데이트 (S3 key 저장)
        user.updateProfile(command.imageKey, user.bio)
        userRepository.save(user)

        return ProfileImageUploadResponse(profileImageUrl = imageUrl)
    }
}

