package com.onuldo.domain.record

import com.onuldo.domain.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Index
import jakarta.persistence.Table
import java.time.LocalDate

/**
 * 기록 엔티티
 */
@Entity
@Table(
    name = "records",
    indexes = [
        jakarta.persistence.Index(name = "idx_record_user", columnList = "user_id"),
        jakarta.persistence.Index(name = "idx_record_hobby", columnList = "hobby_id"),
        jakarta.persistence.Index(name = "idx_record_date", columnList = "activity_date"),
        jakarta.persistence.Index(name = "idx_record_visibility", columnList = "visibility")
    ]
)
class Record(
    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Column(name = "hobby_id", nullable = false)
    val hobbyId: Long,

    @Column(name = "timer_id")
    val timerId: Long? = null,

    @Column(name = "duration_seconds", nullable = false)
    val durationSeconds: Int,

    @Column(columnDefinition = "TEXT")
    var memo: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var visibility: RecordVisibility = RecordVisibility.PUBLIC,

    @Column(name = "activity_date", nullable = false)
    val activityDate: LocalDate = LocalDate.now()
) : BaseEntity() {

    fun updateMemo(newMemo: String?) {
        this.memo = newMemo
    }

    fun changeVisibility(newVisibility: RecordVisibility) {
        this.visibility = newVisibility
    }
}

