package com.todo.mirujima_kotlin.common.config

import com.todo.mirujima_kotlin.auth.entity.CustomUserDetail
import com.todo.mirujima_kotlin.common.exception.AlertException
import com.todo.mirujima_kotlin.user.entity.User
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.security.core.context.SecurityContextHolder
import java.util.*

@Configuration
@EnableJpaAuditing
class AuditorConfig {

    @Bean
    fun auditorAware() :AuditorAwareImpl = AuditorAwareImpl()

    class AuditorAwareImpl : AuditorAware<User> {
        override fun getCurrentAuditor(): Optional<User> {
            val authentication = SecurityContextHolder.getContext().authentication
            if (authentication == null || !authentication.isAuthenticated || authentication.principal == "anonymousUser"
            ) return Optional.empty()
            val userDetail: CustomUserDetail = authentication.principal as CustomUserDetail
            return Optional.of(userDetail.user)
        }

    }
}