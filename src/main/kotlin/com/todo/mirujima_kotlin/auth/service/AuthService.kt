package com.todo.mirujima_kotlin.auth.service

import com.todo.mirujima_kotlin.auth.dto.OAuthUserInfo
import com.todo.mirujima_kotlin.auth.dto.request.LoginRequest
import com.todo.mirujima_kotlin.auth.dto.response.LoginResponse
import com.todo.mirujima_kotlin.auth.dto.response.TokenResponse
import com.todo.mirujima_kotlin.auth.util.JwtUtil
import com.todo.mirujima_kotlin.common.exception.AlertException
import com.todo.mirujima_kotlin.user.dto.response.UserResponse
import com.todo.mirujima_kotlin.user.entity.OauthPlatform
import com.todo.mirujima_kotlin.user.entity.User
import com.todo.mirujima_kotlin.user.repository.UserRepository
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.util.Locale

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
class AuthService(
    private val jwtUtil: JwtUtil,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val oAuthServiceFactory: OAuthServiceFactory
) {

    fun login(loginRequest: LoginRequest): LoginResponse {
        val email = loginRequest.email
        val user = userRepository.findUserByEmail(email)
            .orElseThrow { UsernameNotFoundException("가입되지 않은 이메일입니다.") }
        if (!passwordEncoder.matches(loginRequest.password, user.password)) {
            throw AlertException("비밀번호가 일치하지 않습니다.")
        }
        val userDto = UserResponse.of(user)
        return LoginResponse(
            userDto,
            jwtUtil.createAccessToken(email),
            jwtUtil.createRefreshToken(email),
            jwtUtil.getExpiredAt()
        )
    }

    fun refresh(refreshToken: String): TokenResponse {
        val accessToken = jwtUtil.refreshAccessToken(refreshToken)
        return TokenResponse(
            accessToken,
            refreshToken,
            jwtUtil.getExpiredAt()
        )
    }

    @Transactional
    fun authenticateWithOAuth(platform: String, code: String): LoginResponse {
        var platform = platform
        platform = platform.uppercase(Locale.getDefault())
        val userInfo: OAuthUserInfo = oAuthServiceFactory.getUserInfo(platform, code)
        val email = userInfo.email
        val name = userInfo.name

        val userOptional = userRepository.findUserByEmail(email)
        val user: User
        if (userOptional.isPresent) {
            user = userOptional.get()
        } else {
            user = User(
                email,
                platform,
                name,
                OauthPlatform.valueOf(platform)
            )
            userRepository.save(user)
        }
        return LoginResponse(
            UserResponse.of(user),
            jwtUtil.createAccessToken(email),
            jwtUtil.createRefreshToken(email),
            jwtUtil.getExpiredAt()
        )
    }
}
