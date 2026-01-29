package com.onuldo.application.reaction

import com.onuldo.common.exception.CustomException
import com.onuldo.common.exception.ErrorCode
import com.onuldo.domain.reaction.EmojiType
import com.onuldo.port.inbound.reaction.usecase.RemoveReactionUseCase
import com.onuldo.port.outbound.reaction.ReactionRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RemoveReactionService(
    private val reactionRepository: ReactionRepository
) : RemoveReactionUseCase {

    @Transactional
    override fun execute(userId: Long, recordId: Long, emojiType: EmojiType) {
        val reaction = reactionRepository.findByUserIdAndRecordIdAndEmojiType(userId, recordId, emojiType)
            ?: throw CustomException(ErrorCode.REACTION_NOT_FOUND)

        reactionRepository.delete(reaction)
    }
}
