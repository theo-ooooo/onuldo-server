package com.onuldo.port.inbound.reaction.usecase

import com.onuldo.port.inbound.reaction.model.ReactionCountResponse

interface GetReactionCountUseCase {
    fun execute(recordId: Long): ReactionCountResponse
}
