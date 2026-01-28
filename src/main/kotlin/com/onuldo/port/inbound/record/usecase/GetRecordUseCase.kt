package com.onuldo.port.inbound.record.usecase

import com.onuldo.port.inbound.record.model.RecordResponse

interface GetRecordUseCase {
    fun execute(userId: Long, recordId: Long): RecordResponse
}
