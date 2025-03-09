package com.todo.mirujima_kotlin.user.entity

import com.todo.mirujima_kotlin.common.exception.AlertException
import com.todo.mirujima_kotlin.user.dto.request.ModificationImageRequest
import com.todo.mirujima_kotlin.user.dto.request.ModificationRequest
import jakarta.persistence.*
import org.apache.commons.lang3.StringUtils
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.LocalDateTime

@Entity
@Table(name = "user")
@EntityListeners(AuditingEntityListener::class)
class User(
    @Column(nullable = false, length = 30, unique = true)
    var email: String,
    @Column(nullable = false, length = 100)
    var password: String,
    @Column(nullable = false, length = 30)
    var username: String,
    @Enumerated(EnumType.STRING)
    var oauthPlatform: OauthPlatform,
    @CreatedDate
    var createdAt: LocalDateTime? = null,
    @LastModifiedDate
    var updatedAt: LocalDateTime? = null
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    var orgFileName: String? = null
    var profileImagePath: String? = null

    fun modify(modificationRequest: ModificationRequest, passwordEncoder: PasswordEncoder) {
        if (modificationRequest.email != null) {
            this.email = modificationRequest.email!!
        }
        if (modificationRequest.username != null) {
            this.username = modificationRequest.username!!
        }
        if (modificationRequest.password != null) {
            this.password = passwordEncoder.encode(modificationRequest.password)
        }
        this.updatedAt = LocalDateTime.now()
    }

    fun updateProfileImage(modificationImageRequest: ModificationImageRequest) {
        val orgFileName = modificationImageRequest.orgFileName
        val profileImagePath = modificationImageRequest.profileImagePath
        if (profileImagePath != null) {
            if (StringUtils.isEmpty(orgFileName)) throw AlertException("프로필 이미지 수정시 원본 파일 이름은 필수입니다.")
            this.orgFileName = orgFileName
            this.profileImagePath = profileImagePath
        } else {
            this.orgFileName = null
            this.profileImagePath = null
        }
    }

}