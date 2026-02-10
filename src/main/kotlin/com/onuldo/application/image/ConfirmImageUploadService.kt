package com.onuldo.application.image

import com.onuldo.adapter.outbound.storage.S3FileStorage
import com.onuldo.common.exception.CustomException
import com.onuldo.common.exception.ErrorCode
import com.onuldo.common.exception.ResourceNotFoundException
import com.onuldo.domain.record.RecordImage
import com.onuldo.port.inbound.image.model.ConfirmImageUploadCommand
import com.onuldo.port.inbound.image.model.ImageUploadResponse
import com.onuldo.port.inbound.image.usecase.ConfirmImageUploadUseCase
import com.onuldo.port.outbound.record.RecordImageRepository
import com.onuldo.port.outbound.record.RecordRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ConfirmImageUploadService(
    private val recordRepository: RecordRepository,
    private val recordImageRepository: RecordImageRepository,
    private val s3FileStorage: S3FileStorage
) : ConfirmImageUploadUseCase {

    @Transactional
    override fun execute(command: ConfirmImageUploadCommand): ImageUploadResponse {
        // Validate record exists and belongs to user
        val record = recordRepository.findById(command.recordId)
            ?: throw ResourceNotFoundException("기록", command.recordId, ErrorCode.RECORD_NOT_FOUND)

        if (record.userId != command.userId) {
            throw CustomException(ErrorCode.AUTH_FORBIDDEN, "기록에 이미지를 업로드할 권한이 없습니다.")
        }

        // Verify file exists in S3
        if (!s3FileStorage.exists(command.imageKey)) {
            throw CustomException(ErrorCode.COMMON_INVALID_INPUT, "업로드된 이미지를 찾을 수 없습니다.")
        }

        // Generate presigned download URL
        val imageUrl = s3FileStorage.generatePresignedDownloadUrl(command.imageKey, 60 * 24 * 7) // 7일

        // Save record image entity
        val imageUuid = command.imageKey.substringAfterLast('/').substringBeforeLast('.')
        val recordImage = RecordImage(
            userId = command.userId,
            recordId = command.recordId,
            imageId = imageUuid,
            imageUrl = command.imageKey, // S3 key 저장
            fileName = command.fileName,
            fileSize = command.fileSize,
            contentType = "image/webp",
            width = command.width,
            height = command.height,
            displayOrder = recordImageRepository.findByRecordId(command.recordId).size
        )

        val savedImage = recordImageRepository.save(recordImage)
        val imageId = savedImage.id ?: throw CustomException(ErrorCode.COMMON_INTERNAL_SERVER_ERROR, "이미지 ID가 없습니다.")

        return ImageUploadResponse(
            imageId = imageId,
            imageUrl = imageUrl, // Presigned URL 반환
            fileName = command.fileName,
            fileSize = command.fileSize,
            width = command.width,
            height = command.height
        )
    }
}

