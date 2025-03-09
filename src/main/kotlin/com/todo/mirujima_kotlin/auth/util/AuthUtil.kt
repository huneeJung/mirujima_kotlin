package com.todo.mirujima_kotlin.auth.util

import com.todo.mirujima_kotlin.auth.entity.CustomUserDetail
import com.todo.mirujima_kotlin.common.entity.BaseUserEntity
import com.todo.mirujima_kotlin.common.exception.AlertException
import com.todo.mirujima_kotlin.user.entity.User
import org.apache.commons.lang3.StringUtils
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder

object AuthUtil {

    val authentication: Authentication
        get() = SecurityContextHolder.getContext().getAuthentication()

    val userInfo: User
        get() {
            try {
                val userDetails: CustomUserDetail = authentication.principal as CustomUserDetail
                return userDetails.user
            } catch (e: Exception) {
                throw AlertException("로그인이 필요합니다.")
            }
        }

    val email: String
        get() {
            val authentication: Authentication = SecurityContextHolder.getContext().authentication
            val email = authentication.name
            return if (StringUtils.isEmpty(email)) "" else email
        }

    fun <T : BaseUserEntity> checkAuthority(email: String?, type: String, entity: T) {
        if (entity.createdBy.email != email) {
            throw AlertException(type + "에 대한 권한이 없습니다.")
        }
    }
}
