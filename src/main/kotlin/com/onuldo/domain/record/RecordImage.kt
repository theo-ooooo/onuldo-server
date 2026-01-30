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
        Index(name = "idx_record_image_record", columnList = "record_id"),
        Index(name = "idx_record_image_user", columnList = "user_id")
    ]
)
class RecordImage(
    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Column(name = "record_id", nullable = false)
    val recordId: Long,

    @Column(name = "image_url", nullable = false, length = 500)
    val imageUrl: String,

    @Column(name = "file_name", nullable = false, length = 255)
    val fileName: String,

    @Column(name = "file_size", nullable = false)
    val fileSize: Long,

    @Column(name = "content_type", nullable = false, length = 50)
    val contentType: String,

    @Column(name = "width")
    val width: Int? = null,

    @Column(name = "height")
    val height: Int? = null,

    @Column(name = "display_order", nullable = false)
    var displayOrder: Int = 0
) : BaseEntity()

