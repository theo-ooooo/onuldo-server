package com.onuldo.port.outbound.tag

import com.onuldo.domain.tag.Tag

interface TagRepository {
    fun save(tag: Tag): Tag
    fun findById(id: Long): Tag?
    fun findByName(name: String): Tag?
    fun findByNameIn(names: List<String>): List<Tag>
    fun findByIds(ids: List<Long>): List<Tag>
    fun findPopularTags(limit: Int): List<Tag>
}
