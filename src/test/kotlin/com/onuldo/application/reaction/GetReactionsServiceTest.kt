package com.onuldo.application.reaction

import com.onuldo.domain.reaction.EmojiType
import com.onuldo.domain.reaction.Reaction
import com.onuldo.domain.user.User
import com.onuldo.port.outbound.reaction.ReactionRepository
import com.onuldo.port.outbound.user.UserRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
class GetReactionsServiceTest {

    @Mock
    private lateinit var reactionRepository: ReactionRepository

    @Mock
    private lateinit var userRepository: UserRepository

    private lateinit var getReactionsService: GetReactionsService

    @BeforeEach
    fun setUp() {
        getReactionsService = GetReactionsService(reactionRepository, userRepository)
    }

    @Test
    @DisplayName("리액션 목록 조회 성공")
    fun `should get reactions successfully`() {
        // given
        val recordId = 1L
        val user1 = createUser(1L, "user1")
        val user2 = createUser(2L, "user2")

        val reaction1 = createReaction(1L, user1.id!!, recordId, EmojiType.HEART)
        val reaction2 = createReaction(2L, user2.id!!, recordId, EmojiType.FIRE)

        whenever(reactionRepository.findAllByRecordId(recordId))
            .thenReturn(listOf(reaction1, reaction2))
        whenever(userRepository.findById(user1.id!!)).thenReturn(user1)
        whenever(userRepository.findById(user2.id!!)).thenReturn(user2)

        // when
        val result = getReactionsService.execute(recordId)

        // then
        assertEquals(2, result.size)
        assertEquals(user1.nickname, result[0].nickname)
        assertEquals(EmojiType.HEART, result[0].emojiType)
        assertEquals(user2.nickname, result[1].nickname)
        assertEquals(EmojiType.FIRE, result[1].emojiType)
    }

    @Test
    @DisplayName("리액션이 없는 경우 빈 목록 반환")
    fun `should return empty list when no reactions`() {
        // given
        val recordId = 1L

        whenever(reactionRepository.findAllByRecordId(recordId))
            .thenReturn(emptyList())

        // when
        val result = getReactionsService.execute(recordId)

        // then
        assertEquals(0, result.size)
    }

    private fun createUser(id: Long, nickname: String): User {
        val user = User(
            email = "$nickname@test.com",
            password = "password",
            nickname = nickname
        )
        val idField = user::class.java.superclass.getDeclaredField("id")
        idField.isAccessible = true
        idField.set(user, id)
        return user
    }

    private fun createReaction(id: Long, userId: Long, recordId: Long, emojiType: EmojiType): Reaction {
        val reaction = Reaction(userId = userId, recordId = recordId, emojiType = emojiType)
        val idField = reaction::class.java.superclass.getDeclaredField("id")
        idField.isAccessible = true
        idField.set(reaction, id)
        return reaction
    }
}
