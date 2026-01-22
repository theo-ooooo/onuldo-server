package com.onuldo.domain.record

import com.onuldo.domain.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Index
import jakarta.persistence.Table

/**
 * 기록 이미지 엔티티
 */
@Entity
@Table(
    name = "record_images",
    indexes = [
        jakarta.persistence.Index(name = "idx_record_image_record", columnList = "record_id")
    ]
)
class RecordImage(
    @Column(name = "record_id", nullable = false)
    val recordId: Long,

    @Column(name = "image_url", nullable = false, length = 255)
    val imageUrl: String,

    @Column(name = "display_order", nullable = false)
    val displayOrder: Int = 0
) : BaseEntity()

