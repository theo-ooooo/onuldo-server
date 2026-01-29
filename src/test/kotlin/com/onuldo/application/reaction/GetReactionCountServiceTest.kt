package com.onuldo.application.reaction

import com.onuldo.domain.reaction.EmojiType
import com.onuldo.port.outbound.reaction.ReactionRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
class GetReactionCountServiceTest {

    @Mock
    private lateinit var reactionRepository: ReactionRepository

    private lateinit var getReactionCountService: GetReactionCountService

    @BeforeEach
    fun setUp() {
        getReactionCountService = GetReactionCountService(reactionRepository)
    }

    @Test
    @DisplayName("리액션 통계 조회 성공")
    fun `should get reaction count successfully`() {
        // given
        val recordId = 1L
        val counts = mapOf(
            EmojiType.HEART to 5L,
            EmojiType.FIRE to 3L,
            EmojiType.CLAP to 2L
        )

        whenever(reactionRepository.countByRecordIdGroupByEmojiType(recordId))
            .thenReturn(counts)

        // when
        val result = getReactionCountService.execute(recordId)

        // then
        assertEquals(recordId, result.recordId)
        assertEquals(5L, result.counts[EmojiType.HEART])
        assertEquals(3L, result.counts[EmojiType.FIRE])
        assertEquals(2L, result.counts[EmojiType.CLAP])
        assertEquals(10L, result.totalCount)
    }

    @Test
    @DisplayName("리액션이 없는 경우 빈 통계 반환")
    fun `should return empty counts when no reactions`() {
        // given
        val recordId = 1L

        whenever(reactionRepository.countByRecordIdGroupByEmojiType(recordId))
            .thenReturn(emptyMap())

        // when
        val result = getReactionCountService.execute(recordId)

        // then
        assertEquals(recordId, result.recordId)
        assertEquals(0, result.counts.size)
        assertEquals(0L, result.totalCount)
    }
}
