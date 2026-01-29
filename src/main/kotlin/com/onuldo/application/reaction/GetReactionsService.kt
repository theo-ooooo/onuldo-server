package com.onuldo.application.reaction

import com.onuldo.port.inbound.reaction.model.ReactionWithUserResponse
import com.onuldo.port.inbound.reaction.usecase.GetReactionsUseCase
import com.onuldo.port.outbound.reaction.ReactionRepository
import com.onuldo.port.outbound.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetReactionsService(
    private val reactionRepository: ReactionRepository,
    private val userRepository: UserRepository
) : GetReactionsUseCase {

    @Transactional(readOnly = true)
    override fun execute(recordId: Long): List<ReactionWithUserResponse> {
        val reactions = reactionRepository.findAllByRecordId(recordId)

        val userIds = reactions.map { it.userId }.distinct()
        val usersMap = userIds.mapNotNull { userRepository.findById(it) }
            .associateBy { it.id }

        return reactions.mapNotNull { reaction ->
            val user = usersMap[reaction.userId] ?: return@mapNotNull null
            ReactionWithUserResponse(
                id = reaction.id!!,
                userId = user.id!!,
                nickname = user.nickname,
                profileImageUrl = user.profileImageUrl,
                emojiType = reaction.emojiType,
                createdAt = reaction.createdAt
            )
        }
    }
}
