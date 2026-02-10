package com.onuldo.application.image

import com.onuldo.adapter.outbound.storage.S3FileStorage
import com.onuldo.common.exception.CustomException
import com.onuldo.common.exception.ErrorCode
import com.onuldo.common.exception.ResourceNotFoundException
import com.onuldo.port.inbound.image.model.PresignedUploadUrlResponse
import com.onuldo.port.inbound.image.usecase.GeneratePresignedUploadUrlUseCase
import com.onuldo.port.outbound.record.RecordRepository
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class GeneratePresignedUploadUrlService(
    private val recordRepository: RecordRepository,
    private val s3FileStorage: S3FileStorage,
    private val environment: Environment
) : GeneratePresignedUploadUrlUseCase {

    @Transactional(readOnly = true)
    override fun execute(userId: Long, recordId: Long, fileName: String, contentType: String): PresignedUploadUrlResponse {
        // Validate record exists and belongs to user
        val record = recordRepository.findById(recordId)
            ?: throw ResourceNotFoundException("기록", recordId, ErrorCode.RECORD_NOT_FOUND)

        if (record.userId != userId) {
            throw CustomException(ErrorCode.AUTH_FORBIDDEN, "기록에 이미지를 업로드할 권한이 없습니다.")
        }

        // Generate file name
        val profile = environment.activeProfiles.firstOrNull() ?: "local"
        val imageId = UUID.randomUUID().toString()
        val imageKey = "${profile}/records/${recordId}/${imageId}.webp"

        // Generate presigned URL
        val uploadUrl = s3FileStorage.generatePresignedUploadUrl(imageKey, "image/webp", 5)

        return PresignedUploadUrlResponse(
            uploadUrl = uploadUrl,
            imageKey = imageKey,
            expiresIn = 5
        )
    }
}

