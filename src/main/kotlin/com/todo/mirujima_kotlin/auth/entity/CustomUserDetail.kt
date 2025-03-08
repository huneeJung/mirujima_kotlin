package com.todo.mirujima_kotlin.auth.entity

import com.todo.mirujima_kotlin.user.entity.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class CustomUserDetail (
    val user : User
) : UserDetails {
    val id = user.id
    override fun getAuthorities() = emptyList<GrantedAuthority>()
    override fun getPassword() = user.password
    override fun getUsername() = user.email
}