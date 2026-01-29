package com.onuldo.application.reaction

import com.onuldo.common.exception.CustomException
import com.onuldo.common.exception.ErrorCode
import com.onuldo.domain.reaction.EmojiType
import com.onuldo.domain.reaction.Reaction
import com.onuldo.port.outbound.reaction.ReactionRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
class RemoveReactionServiceTest {

    @Mock
    private lateinit var reactionRepository: ReactionRepository

    private lateinit var removeReactionService: RemoveReactionService

    @BeforeEach
    fun setUp() {
        removeReactionService = RemoveReactionService(reactionRepository)
    }

    @Test
    @DisplayName("리액션 제거 성공")
    fun `should remove reaction successfully`() {
        // given
        val userId = 1L
        val recordId = 1L
        val emojiType = EmojiType.HEART
        val reaction = Reaction(userId = userId, recordId = recordId, emojiType = emojiType)

        whenever(reactionRepository.findByUserIdAndRecordIdAndEmojiType(userId, recordId, emojiType))
            .thenReturn(reaction)

        // when
        removeReactionService.execute(userId, recordId, emojiType)

        // then
        verify(reactionRepository).delete(reaction)
    }

    @Test
    @DisplayName("존재하지 않는 리액션 제거 시 예외 발생")
    fun `should throw exception when reaction not found`() {
        // given
        val userId = 1L
        val recordId = 1L
        val emojiType = EmojiType.HEART

        whenever(reactionRepository.findByUserIdAndRecordIdAndEmojiType(userId, recordId, emojiType))
            .thenReturn(null)

        // when & then
        val exception = assertThrows(CustomException::class.java) {
            removeReactionService.execute(userId, recordId, emojiType)
        }
        assertEquals(ErrorCode.REACTION_NOT_FOUND, exception.errorCode)
    }
}
