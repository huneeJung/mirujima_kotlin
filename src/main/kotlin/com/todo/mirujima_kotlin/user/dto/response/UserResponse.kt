package com.todo.mirujima_kotlin.user.dto.response

import com.todo.mirujima_kotlin.user.entity.User
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class UserResponse(
    @Schema(description = "사용자 고유키", example = "1")
    var id : Long? = null,
    @Schema(description = "사용자의 유저네임", example = "test")
    var username : String? = null,
    @Schema(description = "사용자의 이메일", example = "test@test.com")
    var email : String? = null,
    @Schema(description = "사용자의 유저 프로필 이미지 PATH", example = "2024/01/11/uuid-profile.jpg", nullable = false)
    var profileImagePath: String? = null,
    @Schema(description = "가입 일자", example = "2024-10-11 15:21:00")
    var createdAt: LocalDateTime? = null,
    @Schema(description = "수정 일자", example = "2024-10-11 15:21:00")
    var updatedAt: LocalDateTime? = null,
){
    companion object {
        fun of(user: User): UserResponse {
            return UserResponse().apply {
                id = user.id
                username = user.username
                email = user.email
                profileImagePath = user.profileImagePath
                createdAt = user.createdAt
                updatedAt = user.updatedAt
            }
        }
    }
}