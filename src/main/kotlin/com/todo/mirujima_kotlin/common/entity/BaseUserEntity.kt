package com.todo.mirujima_kotlin.common.entity

import com.todo.mirujima_kotlin.user.entity.User
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
class BaseUserEntity (
    @CreatedBy
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "created_by")
    protected var createdBy: User,
    @LastModifiedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    protected var updatedBy: User
)