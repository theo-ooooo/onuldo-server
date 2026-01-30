package com.onuldo.domain.user

import com.onuldo.domain.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Index
import jakarta.persistence.Table

/**
 * 사용자 엔티티
 *
 * - 이메일 기반 로그인 가정
 * - 소프트 삭제를 위해 상태 필드 사용
 */
@Entity
@Table(
    name = "users",
    indexes = [
        Index(name = "idx_user_email", columnList = "email", unique = true)
    ]
)
class User(

    @Column(nullable = false, unique = true, length = 100)
    val email: String,

    @Column(nullable = false, length = 255)
    var password: String?,

    @Column(unique = true, nullable = false, length = 50)
    var nickname: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var status: UserStatus = UserStatus.ACTIVE,

    @Column(name = "profile_image_url", length = 255)
    var profileImageUrl: String? = null,

    @Column(name = "bio", length = 255)
    var bio: String? = null
) : BaseEntity() {

    fun changeNickname(newNickname: String) {
        this.nickname = newNickname
    }

    /**
     * 해시된 비밀번호를 전달받는다고 가정
     */
    fun changePassword(encodedPassword: String) {
        this.password = encodedPassword
    }

    fun updateProfile(profileImageUrl: String?, bio: String?) {
        this.profileImageUrl = profileImageUrl
        this.bio = bio
    }

    fun deactivate() {
        this.status = UserStatus.INACTIVE
    }

    fun delete() {
        this.status = UserStatus.DELETED
    }
}




