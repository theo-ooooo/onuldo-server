package com.onuldo.application.user

import com.onuldo.adapter.outbound.storage.S3FileStorage
import com.onuldo.common.exception.CustomException
import com.onuldo.common.exception.ErrorCode
import com.onuldo.common.exception.ResourceNotFoundException
import com.onuldo.port.inbound.image.model.PresignedUploadUrlResponse
import com.onuldo.port.inbound.user.usecase.GenerateProfileImagePresignedUrlUseCase
import com.onuldo.port.outbound.user.UserRepository
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class GenerateProfileImagePresignedUrlService(
    private val userRepository: UserRepository,
    private val s3FileStorage: S3FileStorage,
    private val environment: Environment
) : GenerateProfileImagePresignedUrlUseCase {

    @Transactional(readOnly = true)
    override fun execute(userId: Long, fileName: String, contentType: String): PresignedUploadUrlResponse {
        // 사용자 존재 확인
        val user = userRepository.findById(userId)
            ?: throw ResourceNotFoundException("사용자", userId, ErrorCode.USER_NOT_FOUND)

        // Generate file name
        val profile = environment.activeProfiles.firstOrNull() ?: "local"
        val imageId = UUID.randomUUID().toString()
        val imageKey = "${profile}/profiles/${userId}/${imageId}.webp"

        // Generate presigned URL
        val uploadUrl = s3FileStorage.generatePresignedUploadUrl(imageKey, "image/webp", 5)

        return PresignedUploadUrlResponse(
            uploadUrl = uploadUrl,
            imageKey = imageKey,
            expiresIn = 5
        )
    }
}

