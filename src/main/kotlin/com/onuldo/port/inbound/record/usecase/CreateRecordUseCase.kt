package com.onuldo.port.inbound.record.usecase

import com.onuldo.port.inbound.record.model.CreateRecordCommand
import com.onuldo.port.inbound.record.model.RecordResponse

interface CreateRecordUseCase {
    fun execute(command: CreateRecordCommand): RecordResponse
}
