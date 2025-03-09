package com.todo.mirujima_kotlin.auth.service.oauth

import com.todo.mirujima_kotlin.auth.dto.OAuthUserInfo

interface OAuthService {
    fun getAccessToken(code: String): String
    fun getUserInfo(accessToken: String): OAuthUserInfo
}
