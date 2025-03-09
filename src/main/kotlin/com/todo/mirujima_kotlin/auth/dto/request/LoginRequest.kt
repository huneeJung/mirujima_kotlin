package com.todo.mirujima_kotlin.auth.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty

data class LoginRequest (
    @Schema(description = "사용자의 이메일", example = "test@test.com", nullable = false)
    val email: @NotEmpty @Email String,
    @Schema(description = "사용자의 비밀번호", example = "1234", nullable = false)
    val password: @NotEmpty String
)
