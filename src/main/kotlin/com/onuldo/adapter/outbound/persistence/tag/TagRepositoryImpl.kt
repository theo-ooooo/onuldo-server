package com.onuldo.adapter.outbound.persistence.tag

import com.onuldo.domain.tag.Tag
import com.onuldo.port.outbound.tag.TagRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class TagRepositoryImpl(
    private val tagJpaRepository: TagJpaRepository
) : TagRepository {

    override fun save(tag: Tag): Tag {
        return tagJpaRepository.save(tag)
    }

    override fun findById(id: Long): Tag? {
        return tagJpaRepository.findByIdOrNull(id)
    }

    override fun findByName(name: String): Tag? {
        return tagJpaRepository.findByName(name)
    }

    override fun findByNameIn(names: List<String>): List<Tag> {
        return tagJpaRepository.findByNameIn(names)
    }

    override fun findByIds(ids: List<Long>): List<Tag> {
        return tagJpaRepository.findByIdIn(ids)
    }

    override fun findPopularTags(limit: Int): List<Tag> {
        return tagJpaRepository.findPopularTags(limit)
    }
}
