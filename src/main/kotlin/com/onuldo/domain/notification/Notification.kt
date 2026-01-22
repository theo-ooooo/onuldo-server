package com.onuldo.domain.notification

import com.onuldo.domain.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Index
import jakarta.persistence.Table

/**
 * 알림 엔티티
 */
@Entity
@Table(
    name = "notifications",
    indexes = [
        jakarta.persistence.Index(name = "idx_notification_user", columnList = "user_id"),
        jakarta.persistence.Index(name = "idx_notification_read", columnList = "is_read"),
        jakarta.persistence.Index(name = "idx_notification_created", columnList = "created_at")
    ]
)
class Notification(
    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type", nullable = false, length = 20)
    val notificationType: NotificationType,

    @Column(name = "related_user_id")
    val relatedUserId: Long? = null,

    @Column(name = "related_record_id")
    val relatedRecordId: Long? = null,

    @Column(name = "related_comment_id")
    val relatedCommentId: Long? = null,

    @Column(name = "is_read", nullable = false)
    var isRead: Boolean = false
) : BaseEntity() {

    fun markAsRead() {
        this.isRead = true
    }
}

