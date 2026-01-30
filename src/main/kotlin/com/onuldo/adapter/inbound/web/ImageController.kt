package com.onuldo.adapter.inbound.web

import com.onuldo.adapter.inbound.web.dto.ConfirmImageUploadRequest
import com.onuldo.common.dto.ApiResponse
import com.onuldo.common.security.SecurityUtils
import com.onuldo.port.inbound.image.model.ConfirmImageUploadCommand
import com.onuldo.port.inbound.image.model.ImageUploadResponse
import com.onuldo.port.inbound.image.model.PresignedUploadUrlResponse
import com.onuldo.port.inbound.image.usecase.ConfirmImageUploadUseCase
import com.onuldo.port.inbound.image.usecase.GeneratePresignedUploadUrlUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@Tag(name = "Image", description = "이미지 업로드 API")
@RestController
@RequestMapping("/api/records/{recordId}/images")
class ImageController(
    private val securityUtils: SecurityUtils,
    private val generatePresignedUploadUrlUseCase: GeneratePresignedUploadUrlUseCase,
    private val confirmImageUploadUseCase: ConfirmImageUploadUseCase
) {

    @Operation(summary = "Presigned URL 생성", description = "S3에 직접 업로드하기 위한 Presigned URL을 생성합니다.")
    @GetMapping("/presigned-url")
    fun generatePresignedUploadUrl(
        request: HttpServletRequest,
        @PathVariable recordId: Long,
        @RequestParam fileName: String,
        @RequestParam contentType: String = "image/webp"
    ): ApiResponse<PresignedUploadUrlResponse> {
        val userId = securityUtils.getCurrentUserId(request)
        val response = generatePresignedUploadUrlUseCase.execute(userId, recordId, fileName, contentType)
        return ApiResponse.success(response, message = "Presigned URL을 생성했습니다.")
    }

    @Operation(summary = "이미지 업로드 확인", description = "S3에 업로드 완료된 이미지 정보를 저장합니다.")
    @PostMapping("/confirm")
    @ResponseStatus(HttpStatus.CREATED)
    fun confirmImageUpload(
        request: HttpServletRequest,
        @PathVariable recordId: Long,
        @Valid @RequestBody body: ConfirmImageUploadRequest
    ): ApiResponse<ImageUploadResponse> {
        val userId = securityUtils.getCurrentUserId(request)
        val command = ConfirmImageUploadCommand(
            userId = userId,
            recordId = recordId,
            imageKey = body.imageKey,
            fileName = body.fileName,
            fileSize = body.fileSize,
            width = body.width,
            height = body.height
        )
        val response = confirmImageUploadUseCase.execute(command)
        return ApiResponse.success(response, message = "이미지 업로드를 확인했습니다.")
    }
}

