package com.onuldo.application.image

import com.onuldo.common.exception.CustomException
import com.onuldo.common.exception.ErrorCode
import com.onuldo.common.exception.ResourceNotFoundException
import com.onuldo.common.util.ImageUtil
import com.onuldo.domain.record.RecordImage
import com.onuldo.port.inbound.image.model.ImageUploadResponse
import com.onuldo.port.inbound.image.usecase.UploadImageUseCase
import com.onuldo.port.outbound.record.RecordImageRepository
import com.onuldo.port.outbound.record.RecordRepository
import com.onuldo.port.outbound.storage.FileStorage
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

@Service
class UploadImageService(
    private val recordRepository: RecordRepository,
    private val recordImageRepository: RecordImageRepository,
    private val fileStorage: FileStorage
) : UploadImageUseCase {

    @Transactional
    override fun execute(userId: Long, recordId: Long, file: MultipartFile): ImageUploadResponse {
        // Validate record exists and belongs to user
        val record = recordRepository.findById(recordId)
            ?: throw ResourceNotFoundException("기록", recordId, ErrorCode.RECORD_NOT_FOUND)

        if (record.userId != userId) {
            throw CustomException(ErrorCode.AUTH_FORBIDDEN, "기록에 이미지를 업로드할 권한이 없습니다.")
        }

        // Validate file
        if (!ImageUtil.validateImageFile(file)) {
            throw CustomException(ErrorCode.COMMON_INVALID_INPUT, "이미지 파일만 업로드할 수 있습니다.")
        }

        if (!ImageUtil.validateFileSize(file)) {
            throw CustomException(ErrorCode.COMMON_INVALID_INPUT, "파일 크기는 10MB 이하여야 합니다.")
        }

        // Generate file name
        val originalFileName = file.originalFilename ?: "image"
        val extension = originalFileName.substringAfterLast('.', "")
        val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
        val uuid = UUID.randomUUID().toString().substring(0, 8)
        val fileName = "record_${recordId}_${timestamp}_${uuid}.${extension}"

        // Get image dimensions
        val (width, height) = try {
            ImageUtil.getImageDimensions(file.inputStream)
        } catch (e: Exception) {
            null to null
        }

        // Resize image if needed (WebP로 변환)
        val resizedImageBytes = try {
            ImageUtil.resizeImageToWebP(file.inputStream, 1920, 1920)
        } catch (e: Exception) {
            throw CustomException(ErrorCode.COMMON_INTERNAL_SERVER_ERROR, "이미지 처리에 실패했습니다: ${e.message}")
        }

        // Create thumbnail (WebP로 변환)
        val thumbnailBytes = try {
            ImageUtil.createThumbnailToWebP(ByteArrayInputStream(resizedImageBytes))
        } catch (e: Exception) {
            null
        }

        // Save resized image (WebP 형식)
        val directory = "records/${recordId}"
        val contentType = "image/webp"
        val imageUrl = fileStorage.saveFile(resizedImageBytes, directory, fileName, contentType)

        // Save thumbnail if created (WebP 형식)
        val thumbnailUrl = thumbnailBytes?.let {
            val thumbnailFileName = "thumb_$fileName"
            fileStorage.saveFile(it, directory, thumbnailFileName, "image/webp")
        }

        // Save record image entity
        val recordImage = RecordImage(
            userId = userId,
            recordId = recordId,
            imageUrl = imageUrl,
            thumbnailUrl = thumbnailUrl,
            fileName = originalFileName,
            fileSize = file.size,
            contentType = "image/webp",
            width = width,
            height = height,
            displayOrder = recordImageRepository.findByRecordId(recordId).size
        )

        val savedImage = recordImageRepository.save(recordImage)
        val imageId = savedImage.id ?: throw CustomException(ErrorCode.COMMON_INTERNAL_SERVER_ERROR, "이미지 ID가 없습니다.")

        return ImageUploadResponse(
            imageId = imageId,
            imageUrl = imageUrl,
            thumbnailUrl = thumbnailUrl,
            fileName = originalFileName,
            fileSize = file.size,
            width = width,
            height = height
        )
    }
}

