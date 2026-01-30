package com.onuldo.adapter.inbound.web.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive

data class ConfirmImageUploadRequest(
    @field:NotBlank(message = "이미지 키는 필수입니다.")
    val imageKey: String,

    @field:NotBlank(message = "파일명은 필수입니다.")
    val fileName: String,

    @field:NotNull(message = "파일 크기는 필수입니다.")
    @field:Positive(message = "파일 크기는 양수여야 합니다.")
    val fileSize: Long,

    val width: Int? = null,
    val height: Int? = null
)

