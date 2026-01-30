package com.onuldo.port.inbound.statistics.usecase

import com.onuldo.port.inbound.statistics.model.CalendarRecordResponse
import java.time.YearMonth

interface GetCalendarRecordsUseCase {
    fun execute(userId: Long, yearMonth: YearMonth): List<CalendarRecordResponse>
}

