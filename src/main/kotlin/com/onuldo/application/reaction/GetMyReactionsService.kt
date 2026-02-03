package com.onuldo.application.reaction

import com.onuldo.port.inbound.reaction.model.ReactionResponse
import com.onuldo.port.inbound.reaction.usecase.GetMyReactionsUseCase
import com.onuldo.port.outbound.reaction.ReactionRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetMyReactionsService(
    private val reactionRepository: ReactionRepository
) : GetMyReactionsUseCase {

    @Transactional(readOnly = true)
    override fun execute(userId: Long, recordId: Long): List<ReactionResponse> {
        val reactions = reactionRepository.findAllByUserIdAndRecordId(userId, recordId)
        return reactions.map { ReactionResponse.from(it) }
    }
}

