package com.todo.mirujima_kotlin.auth.dto.response

import com.todo.mirujima_kotlin.user.dto.response.UserResponse
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class LoginResponse (
    @Schema(description = "사용자 정보")
    private val user: UserResponse? = null,
    @Schema(
        description = "accessToken",
        example = "eyJhbGciOiJIUzI1NiJ9.eyJsb2dpbklkIjoic3RyaW5nIiwidHlwZSI6ImFjY2VzcyIsImlhdCI6MTczMjgwMzI0OSwiZXhwIjoxNzMyODAzODQ5fQ.F3hjvzGHgoahAAUUe3M44UfU8eceSHHdl4LFkH8GBjQ"
    )
    private val accessToken: String,
    @Schema(
        description = "refreshToken",
        example = "eyJhbGciOiJIUzI1NiJ9.eyJsb2dpbklkIjoic3RyaW5nIiwidHlwZSI6ImFjY2VzcyIsImlhdCI6MTczMjgwMzI0OSwiZXhwIjoxNzMyODAzODQ5fQ.F3hjvzGHgoahAAUUe3M44UfU8eceSHHdl4LFkH8GBjQ"
    )
    private val refreshToken: String,
    @Schema(description = "refreshToken 만료 시간", example = "2021-11-01T00:00:00")
    private val expiredAt: LocalDateTime
)
