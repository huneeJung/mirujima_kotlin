package com.todo.mirujima_kotlin.auth.dto

import lombok.Builder

data class OAuthUserInfo(
    val email: String,
    val name: String
)
