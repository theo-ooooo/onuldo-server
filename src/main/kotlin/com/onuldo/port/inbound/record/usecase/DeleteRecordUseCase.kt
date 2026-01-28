package com.onuldo.port.inbound.record.usecase

interface DeleteRecordUseCase {
    fun execute(userId: Long, recordId: Long)
}
