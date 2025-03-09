package com.todo.mirujima_kotlin.auth.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Getter
import lombok.NoArgsConstructor
import java.time.LocalDateTime

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
data class TokenResponse (
    @Schema(
        description = "accessToken",
        example = "eyJhbGciOiJIUzI1NiJ9.eyJsb2dpbklkIjoic3RyaW5nIiwidHlwZSI6ImFjY2VzcyIsImlhdCI6MTczMjgwMzI0OSwiZXhwIjoxNzMyODAzODQ5fQ.F3hjvzGHgoahAAUUe3M44UfU8eceSHHdl4LFkH8GBjQ"
    )
    private val accessToken: String,
    @Schema(
        description = "refreshToken",
        example = "eyJhbGciOiJIUzI1NiJ9.eyJsb2dpbklkIjoic3RyaW5nIiwidHlwZSI6ImFjY2VzcyIsImlhdCI6MTczMjgwMzI0OSwiZXhwIjoxNzMyODAzODQ5fQ.F3hjvzGHgoahAAUUe3M44UfU8eceSHHdl4LFkH8GBjQ"
    )
    private val refreshToken,
    @Schema(description = "refreshToken 만료 시간", example = "2021-11-01T00:00:00")
    private val expiredAt: LocalDateTime
)
