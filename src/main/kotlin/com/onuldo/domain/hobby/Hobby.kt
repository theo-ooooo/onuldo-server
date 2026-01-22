package com.onuldo.domain.hobby

import com.onuldo.domain.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Index
import jakarta.persistence.Table

/**
 * 취미 엔티티
 */
@Entity
@Table(
    name = "hobbies",
    indexes = [
        jakarta.persistence.Index(name = "idx_hobby_name", columnList = "name", unique = true)
    ]
)
class Hobby(
    @Column(nullable = false, unique = true, length = 50)
    val name: String,

    @Column(columnDefinition = "TEXT")
    val description: String? = null,

    @Column(name = "icon_url", length = 255)
    val iconUrl: String? = null,

    @Column(name = "color_code", length = 7)
    val colorCode: String? = null,

    @Column(name = "is_active", nullable = false)
    var isActive: Boolean = true
) : BaseEntity() {
    fun deactivate() {
        this.isActive = false
    }

    fun activate() {
        this.isActive = true
    }
}

