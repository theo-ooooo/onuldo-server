package com.onuldo.application.reaction

import com.onuldo.application.notification.CreateNotificationService
import com.onuldo.common.exception.CustomException
import com.onuldo.common.exception.ErrorCode
import com.onuldo.domain.reaction.Reaction
import com.onuldo.port.inbound.reaction.model.AddReactionCommand
import com.onuldo.port.inbound.reaction.model.ReactionResponse
import com.onuldo.port.inbound.reaction.usecase.AddReactionUseCase
import com.onuldo.port.outbound.reaction.ReactionRepository
import com.onuldo.port.outbound.record.RecordRepository
import com.onuldo.port.outbound.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AddReactionService(
    private val reactionRepository: ReactionRepository,
    private val recordRepository: RecordRepository,
    private val userRepository: UserRepository,
    private val createNotificationService: CreateNotificationService
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

        // 알림 생성 (비동기)
        val record = recordRepository.findById(command.recordId)
        val actor = userRepository.findById(command.userId)
        if (record != null && actor != null && record.userId != command.userId) {
            createNotificationService.createReactionNotification(
                recordOwnerId = record.userId,
                actorUserId = command.userId,
                actorNickname = actor.nickname,
                recordId = command.recordId,
                emojiType = command.emojiType.name
            )
        }

        return ReactionResponse.from(savedReaction)
    }
}
