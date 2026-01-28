package com.onuldo.adapter.inbound.web.dto

import com.onuldo.domain.record.RecordVisibility

data class UpdateRecordRequest(
    val memo: String? = null,
    val visibility: RecordVisibility? = null
)
