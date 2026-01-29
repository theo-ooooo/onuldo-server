package com.onuldo.application.reaction

import com.onuldo.common.exception.CustomException
import com.onuldo.common.exception.ErrorCode
import com.onuldo.domain.reaction.Reaction
import com.onuldo.port.inbound.reaction.model.AddReactionCommand
import com.onuldo.port.inbound.reaction.model.ReactionResponse
import com.onuldo.port.inbound.reaction.usecase.AddReactionUseCase
import com.onuldo.port.outbound.reaction.ReactionRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AddReactionService(
    private val reactionRepository: ReactionRepository
) : AddReactionUseCase {

    @Transactional
    override fun execute(command: AddReactionCommand): ReactionResponse {
        if (reactionRepository.existsByUserIdAndRecordIdAndEmojiType(
                command.userId,
                command.recordId,
                command.emojiType
            )
        ) {
            throw CustomException(ErrorCode.REACTION_ALREADY_EXISTS)
        }

        val reaction = Reaction(
            userId = command.userId,
            recordId = command.recordId,
            emojiType = command.emojiType
        )

        val savedReaction = reactionRepository.save(reaction)
        return ReactionResponse.from(savedReaction)
    }
}
