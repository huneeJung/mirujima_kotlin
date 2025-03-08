package com.todo.mirujima_kotlin.auth.filter

import com.todo.mirujima_kotlin.auth.entity.CustomUserDetail
import com.todo.mirujima_kotlin.auth.util.JwtUtil
import io.jsonwebtoken.ExpiredJwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.servlet.HandlerExceptionResolver

class JwtFilter(
    private val jwtUtil: JwtUtil,
    private val userDetailsService :UserDetailsService,
    private val handlerExceptionResolver: HandlerExceptionResolver
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authorization = request.getHeader("Authorization")

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }

        val token = authorization.substring(7)

        if (jwtUtil.isExpired(token)) {
            handlerExceptionResolver.resolveException(
                request,
                response,
                null,
                ExpiredJwtException(null, null, "토큰이 만료되었습니다.")
            )
            return
        }

        val loginId = jwtUtil.getLoginId(token)
        val userDetails: CustomUserDetail = userDetailsService.loadUserByUsername(loginId) as CustomUserDetail

        try {
            if (jwtUtil.validateToken(token, userDetails)) {
                val authToken = UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
                )
                SecurityContextHolder.getContext().authentication = authToken
            }

            filterChain.doFilter(request, response)
        } catch (e: ExpiredJwtException) {
            handlerExceptionResolver.resolveException(
                request,
                response,
                null,
                ExpiredJwtException(null, null, "토큰이 만료되었습니다.")
            )
        }
    }
}