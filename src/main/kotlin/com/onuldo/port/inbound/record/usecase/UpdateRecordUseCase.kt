package com.onuldo.port.inbound.record.usecase

import com.onuldo.port.inbound.record.model.RecordResponse
import com.onuldo.port.inbound.record.model.UpdateRecordCommand

interface UpdateRecordUseCase {
    fun execute(command: UpdateRecordCommand): RecordResponse
}
