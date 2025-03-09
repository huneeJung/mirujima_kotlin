package com.todo.mirujima_kotlin.auth.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import lombok.AllArgsConstructor
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.Setter

data class EmailCheckResponse (
    @Schema(description = "이메일 중복 여부", example = "true")
    val exists: Boolean
)
