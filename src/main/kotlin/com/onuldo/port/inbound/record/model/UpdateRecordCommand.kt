package com.onuldo.port.inbound.record.model

import com.onuldo.domain.record.RecordVisibility

data class UpdateRecordCommand(
    val userId: Long,
    val recordId: Long,
    val memo: String? = null,
    val visibility: RecordVisibility? = null
)
