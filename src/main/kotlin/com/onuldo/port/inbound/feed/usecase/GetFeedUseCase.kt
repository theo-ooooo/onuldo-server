package com.onuldo.port.inbound.feed.usecase

import com.onuldo.port.inbound.feed.model.FeedItemResponse
import com.onuldo.port.inbound.feed.model.FeedQuery

interface GetFeedUseCase {
    fun execute(query: FeedQuery): List<FeedItemResponse>
}
