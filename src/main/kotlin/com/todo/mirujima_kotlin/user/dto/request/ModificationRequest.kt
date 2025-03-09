package com.todo.mirujima_kotlin.user.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import lombok.Getter

data class ModificationRequest (
    @Schema(description = "사용자의 이메일", example = "test@test.com", nullable = false)
    var email: @Email String? = null,
    @Schema(description = "사용자의 유저 네임", example = "test", nullable = false)
    var username: String? = null,
    @Schema(description = "사용자의 비밀번호", example = "1234", nullable = false)
    var password: String? = null
)
