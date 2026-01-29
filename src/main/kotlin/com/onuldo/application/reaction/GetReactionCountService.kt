package com.onuldo.application.reaction

import com.onuldo.port.inbound.reaction.model.ReactionCountResponse
import com.onuldo.port.inbound.reaction.usecase.GetReactionCountUseCase
import com.onuldo.port.outbound.reaction.ReactionRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetReactionCountService(
    private val reactionRepository: ReactionRepository
) : GetReactionCountUseCase {

    @Transactional(readOnly = true)
    override fun execute(recordId: Long): ReactionCountResponse {
        val counts = reactionRepository.countByRecordIdGroupByEmojiType(recordId)
        return ReactionCountResponse.of(recordId, counts)
    }
}
