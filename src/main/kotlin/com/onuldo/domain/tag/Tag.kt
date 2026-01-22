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
        jakarta.persistence.Index(name = "idx_tag_name", columnList = "name", unique = true)
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
        if (this.usageCount > 0) {
            this.usageCount--
        }
    }
}

