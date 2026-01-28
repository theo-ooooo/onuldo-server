package com.onuldo.adapter.outbound.persistence.tag

import com.onuldo.domain.tag.Tag
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface TagJpaRepository : JpaRepository<Tag, Long> {
    fun findByName(name: String): Tag?
    fun findByNameIn(names: List<String>): List<Tag>

    @Query("SELECT t FROM Tag t ORDER BY t.usageCount DESC")
    fun findPopularTags(limit: Int): List<Tag>
}
