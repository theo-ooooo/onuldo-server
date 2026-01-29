package com.onuldo.application.reaction

import com.onuldo.common.exception.CustomException
import com.onuldo.common.exception.ErrorCode
import com.onuldo.domain.reaction.EmojiType
import com.onuldo.domain.reaction.Reaction
import com.onuldo.port.inbound.reaction.model.AddReactionCommand
import com.onuldo.port.outbound.reaction.ReactionRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
class AddReactionServiceTest {

    @Mock
    private lateinit var reactionRepository: ReactionRepository

    private lateinit var addReactionService: AddReactionService

    @BeforeEach
    fun setUp() {
        addReactionService = AddReactionService(reactionRepository)
    }

    @Test
    @DisplayName("리액션 추가 성공")
    fun `should add reaction successfully`() {
        // given
        val command = AddReactionCommand(
            userId = 1L,
            recordId = 1L,
            emojiType = EmojiType.HEART
        )

        whenever(reactionRepository.existsByUserIdAndRecordIdAndEmojiType(
            command.userId, command.recordId, command.emojiType
        )).thenReturn(false)

        whenever(reactionRepository.save(any())).thenAnswer { invocation ->
            val reaction = invocation.getArgument<Reaction>(0)
            Reaction(
                userId = reaction.userId,
                recordId = reaction.recordId,
                emojiType = reaction.emojiType
            ).apply {
                val idField = this::class.java.superclass.getDeclaredField("id")
                idField.isAccessible = true
                idField.set(this, 1L)
            }
        }

        // when
        val result = addReactionService.execute(command)

        // then
        assertEquals(1L, result.userId)
        assertEquals(1L, result.recordId)
        assertEquals(EmojiType.HEART, result.emojiType)
        verify(reactionRepository).save(any())
    }

    @Test
    @DisplayName("중복 리액션 추가 시 예외 발생")
    fun `should throw exception when reaction already exists`() {
        // given
        val command = AddReactionCommand(
            userId = 1L,
            recordId = 1L,
            emojiType = EmojiType.HEART
        )

        whenever(reactionRepository.existsByUserIdAndRecordIdAndEmojiType(
            command.userId, command.recordId, command.emojiType
        )).thenReturn(true)

        // when & then
        val exception = assertThrows(CustomException::class.java) {
            addReactionService.execute(command)
        }
        assertEquals(ErrorCode.REACTION_ALREADY_EXISTS, exception.errorCode)
    }
}
