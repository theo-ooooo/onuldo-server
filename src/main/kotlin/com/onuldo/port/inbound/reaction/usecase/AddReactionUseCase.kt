package com.onuldo.port.inbound.reaction.usecase

import com.onuldo.port.inbound.reaction.model.AddReactionCommand
import com.onuldo.port.inbound.reaction.model.ReactionResponse

interface AddReactionUseCase {
    fun execute(command: AddReactionCommand): ReactionResponse
}
