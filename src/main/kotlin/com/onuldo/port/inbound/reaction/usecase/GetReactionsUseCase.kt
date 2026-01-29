package com.onuldo.port.inbound.reaction.usecase

import com.onuldo.port.inbound.reaction.model.ReactionWithUserResponse

interface GetReactionsUseCase {
    fun execute(recordId: Long): List<ReactionWithUserResponse>
}
