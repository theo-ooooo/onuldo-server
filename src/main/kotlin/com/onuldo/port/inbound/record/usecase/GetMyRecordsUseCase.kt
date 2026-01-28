package com.onuldo.port.inbound.record.usecase

import com.onuldo.port.inbound.record.model.RecordResponse

interface GetMyRecordsUseCase {
    fun execute(userId: Long, page: Int, size: Int): List<RecordResponse>
}
