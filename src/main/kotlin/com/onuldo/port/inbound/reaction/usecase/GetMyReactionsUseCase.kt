package com.onuldo.port.inbound.reaction.usecase

import com.onuldo.port.inbound.reaction.model.ReactionResponse

interface GetMyReactionsUseCase {
    fun execute(userId: Long, recordId: Long): List<ReactionResponse>
}

