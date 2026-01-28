package com.onuldo.adapter.inbound.web.dto

import jakarta.validation.constraints.Size

data class UpdateHobbyRequest(
    @field:Size(max = 50, message = "취미 이름은 50자 이하여야 합니다.")
    val name: String? = null,

    val description: String? = null,

    @field:Size(max = 255, message = "아이콘 URL은 255자 이하여야 합니다.")
    val iconUrl: String? = null,

    @field:Size(max = 7, message = "색상 코드는 7자 이하여야 합니다.")
    val colorCode: String? = null
)
