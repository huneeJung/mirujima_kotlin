package com.todo.mirujima_kotlin.common.config

import com.todo.mirujima_kotlin.auth.filter.JwtFilter
import com.todo.mirujima_kotlin.auth.util.JwtUtil
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.servlet.HandlerExceptionResolver

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
class SecurityConfig(
    val jwtUtil: JwtUtil,
    val userDetailsService : UserDetailsService,
    val handlerExceptionResolver : HandlerExceptionResolver
) {

    @Bean
    fun authenticationManager(configuration: AuthenticationConfiguration) :AuthenticationManager
    = configuration.authenticationManager

    @Bean
    fun passwordEncoder(): PasswordEncoder
    = BCryptPasswordEncoder()

    @Bean
    fun securityFilterChain(http :HttpSecurity) {
        http.cors { cors -> cors.configurationSource(corsConfigurationSource()) }
            .csrf { csrf -> csrf.disable() }
            .formLogin { formLogin -> formLogin.disable() }
            .httpBasic { httpBasic -> httpBasic.disable() }
            .sessionManagement { session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .addFilterBefore(JwtFilter(jwtUtil, userDetailsService, handlerExceptionResolver), UsernamePasswordAuthenticationFilter::class.java)
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html", "/swagger-resources/**", "/actuator/**").permitAll()
                    .requestMatchers("/mirujima/auth/**").permitAll()
                    .requestMatchers("/mirujima/user/**").permitAll()
                    .anyRequest().authenticated()
            }
    }

    @Bean
    fun corsConfigurationSource() :CorsConfigurationSource {
        val configuration = CorsConfiguration().apply {
            allowedMethods = mutableListOf("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
            allowedHeaders = listOf("*")
            allowedOriginPatterns = listOf("*")
            exposedHeaders = mutableListOf("Authorization", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials")
            allowCredentials = true
            maxAge = 3600L
        }
        return UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration("/**", configuration)
        }
    }

}