package com.onuldo.domain.hobby

import com.onuldo.domain.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Index
import jakarta.persistence.Table

/**
 * 취미 엔티티
 *
 * - 유저별로 생성되는 취미
 * - 소프트 삭제를 위해 isActive 필드 사용
 */
@Entity
@Table(
    name = "hobbies",
    indexes = [
        Index(name = "idx_hobby_user_id", columnList = "userId"),
        Index(name = "idx_hobby_user_name", columnList = "userId,name", unique = true)
    ]
)
class Hobby(
    @Column(nullable = false)
    val userId: Long,

    @Column(nullable = false, length = 50)
    var name: String,

    @Column(length = 255)
    var description: String? = null,

    @Column(name = "icon_url", length = 255)
    var iconUrl: String? = null,

    @Column(name = "color_code", length = 7)
    var colorCode: String? = null,

    @Column(name = "is_active", nullable = false)
    var isActive: Boolean = true
) : BaseEntity() {

    fun update(
        name: String? = null,
        description: String? = null,
        iconUrl: String? = null,
        colorCode: String? = null
    ) {
        name?.let { this.name = it }
        description?.let { this.description = it }
        iconUrl?.let { this.iconUrl = it }
        colorCode?.let { this.colorCode = it }
    }

    fun deactivate() {
        this.isActive = false
    }

    fun activate() {
        this.isActive = true
    }
}

