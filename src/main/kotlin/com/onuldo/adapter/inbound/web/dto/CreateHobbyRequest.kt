package com.onuldo.adapter.inbound.web.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateHobbyRequest(
    @field:NotBlank(message = "취미 이름은 필수입니다.")
    @field:Size(max = 50, message = "취미 이름은 50자 이하여야 합니다.")
    val name: String,

    val description: String? = null,

    @field:Size(max = 255, message = "아이콘 URL은 255자 이하여야 합니다.")
    val iconUrl: String? = null,

    @field:Size(max = 7, message = "색상 코드는 7자 이하여야 합니다.")
    val colorCode: String? = null
)
