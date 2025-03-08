package com.todo.mirujima_kotlin.auth.service

import com.todo.mirujima_kotlin.auth.entity.CustomUserDetail
import com.todo.mirujima_kotlin.user.entity.User
import com.todo.mirujima_kotlin.user.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class JpaUserDetailService (
    private val userRepository: UserRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        val userByUsername: User = userRepository.findUserByEmail(username!!)
            .orElseThrow { UsernameNotFoundException("유저를 찾지 못했습니다. : $username") }
        return CustomUserDetail(userByUsername)
    }


}