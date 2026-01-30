package com.onuldo.domain.userhobby

import com.onuldo.domain.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Index
import jakarta.persistence.Table
import java.time.LocalDateTime

/**
 * 사용자-취미 관계 엔티티 (통계용)
 */
@Entity
@Table(
    name = "user_hobbies",
    indexes = [
        Index(name = "idx_user_hobby_user", columnList = "user_id"),
        Index(name = "idx_user_hobby_hobby", columnList = "hobby_id"),
        Index(
            name = "uk_user_hobby",
            columnList = "user_id,hobby_id",
            unique = true
        )
    ]
)
class UserHobby(
    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Column(name = "hobby_id", nullable = false)
    val hobbyId: Long,

    @Column(name = "total_duration_seconds", nullable = false)
    var totalDurationSeconds: Int = 0,

    @Column(name = "record_count", nullable = false)
    var recordCount: Int = 0,

    @Column(name = "first_recorded_at")
    var firstRecordedAt: LocalDateTime? = null,

    @Column(name = "last_recorded_at")
    var lastRecordedAt: LocalDateTime? = null
) : BaseEntity() {

    fun addRecord(durationSeconds: Int) {
        this.totalDurationSeconds += durationSeconds
        this.recordCount++
        val now = LocalDateTime.now()
        if (firstRecordedAt == null) {
            firstRecordedAt = now
        }
        lastRecordedAt = now
    }

    fun removeRecord(durationSeconds: Int) {
        this.totalDurationSeconds = maxOf(0, this.totalDurationSeconds - durationSeconds)
        this.recordCount = maxOf(0, this.recordCount - 1)
    }
}

