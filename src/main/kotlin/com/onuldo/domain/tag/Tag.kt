package com.onuldo.domain.tag

import com.onuldo.domain.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Index
import jakarta.persistence.Table

/**
 * 태그 엔티티
 */
@Entity
@Table(
    name = "tags",
    indexes = [
        Index(name = "idx_tag_name", columnList = "name", unique = true),
        Index(name = "idx_tag_usage_count", columnList = "usage_count")
    ]
)
class Tag(
    @Column(nullable = false, unique = true, length = 50)
    val name: String,

    @Column(name = "usage_count", nullable = false)
    var usageCount: Int = 0
) : BaseEntity() {

    fun incrementUsageCount() {
        this.usageCount++
    }

    fun decrementUsageCount() {
        this.usageCount = maxOf(0, this.usageCount - 1)
    }
}

