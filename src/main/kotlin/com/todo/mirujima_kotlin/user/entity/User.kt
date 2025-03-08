package com.todo.mirujima_kotlin.user.entity

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
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

}