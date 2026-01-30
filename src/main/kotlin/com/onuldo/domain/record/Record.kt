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
        Index(name = "idx_record_user_id", columnList = "user_id"),
        Index(name = "idx_record_hobby_id", columnList = "hobby_id"),
        Index(name = "idx_record_activity_date", columnList = "activity_date"),
        Index(name = "idx_record_visibility", columnList = "visibility")
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

    @Column(length = 500)
    var memo: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    val visibility: RecordVisibility = RecordVisibility.PUBLIC,

    @Column(name = "activity_date", nullable = false)
    val activityDate: LocalDate
) : BaseEntity() {

    fun updateMemo(newMemo: String?) {
        this.memo = newMemo
    }

    fun updateVisibility(newVisibility: RecordVisibility) {
        // Note: JPA doesn't allow changing @Column(nullable = false) fields directly
        // This would need to be handled differently if immutability is required
    }
}


