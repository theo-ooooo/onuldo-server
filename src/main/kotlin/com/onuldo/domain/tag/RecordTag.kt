package com.onuldo.domain.tag

import com.onuldo.domain.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Index
import jakarta.persistence.Table

/**
 * 기록-태그 연결 엔티티
 */
@Entity
@Table(
    name = "record_tags",
    indexes = [
        jakarta.persistence.Index(name = "idx_record_tag_record", columnList = "record_id"),
        jakarta.persistence.Index(name = "idx_record_tag_tag", columnList = "tag_id"),
        jakarta.persistence.Index(
            name = "uk_record_tag",
            columnList = "record_id,tag_id",
            unique = true
        )
    ]
)
class RecordTag(
    @Column(name = "record_id", nullable = false)
    val recordId: Long,

    @Column(name = "tag_id", nullable = false)
    val tagId: Long
) : BaseEntity()

