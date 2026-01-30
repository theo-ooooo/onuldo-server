package com.onuldo.application.image

import com.onuldo.common.exception.CustomException
import com.onuldo.common.exception.ErrorCode
import com.onuldo.common.exception.ResourceNotFoundException
import com.onuldo.port.inbound.image.model.PresignedUploadUrlResponse
import com.onuldo.port.inbound.image.usecase.GeneratePresignedUploadUrlUseCase
import com.onuldo.port.outbound.record.RecordRepository
import com.onuldo.port.outbound.storage.FileStorage
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

@Service
class GeneratePresignedUploadUrlService(
    private val recordRepository: RecordRepository,
    @Qualifier("s3FileStorage") private val s3FileStorage: FileStorage
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
        val originalFileName = fileName
        val extension = "webp" // 항상 webp로 변환
        val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
        val uuid = UUID.randomUUID().toString().substring(0, 8)
        val imageKey = "records/${recordId}/image_${timestamp}_${uuid}.${extension}"

        // Generate presigned URL
        val s3Storage = s3FileStorage as? com.onuldo.adapter.outbound.storage.S3FileStorage
            ?: throw CustomException(ErrorCode.COMMON_INTERNAL_SERVER_ERROR, "S3 스토리지가 설정되지 않았습니다.")

        val uploadUrl = s3Storage.generatePresignedUploadUrl(imageKey, "image/webp", 5)

        return PresignedUploadUrlResponse(
            uploadUrl = uploadUrl,
            imageKey = imageKey,
            expiresIn = 5
        )
    }
}

