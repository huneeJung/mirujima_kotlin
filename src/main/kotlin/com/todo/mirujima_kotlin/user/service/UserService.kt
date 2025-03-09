package com.todo.mirujima_kotlin.user.service

import com.todo.mirujima_kotlin.auth.dto.response.EmailCheckResponse
import com.todo.mirujima_kotlin.common.exception.AlertException
import com.todo.mirujima_kotlin.user.dto.request.ModificationImageRequest
import com.todo.mirujima_kotlin.user.dto.request.ModificationRequest
import com.todo.mirujima_kotlin.user.dto.request.RegisterRequest
import com.todo.mirujima_kotlin.user.dto.response.UserResponse
import com.todo.mirujima_kotlin.user.entity.OauthPlatform
import com.todo.mirujima_kotlin.user.entity.User
import com.todo.mirujima_kotlin.user.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
class UserService (
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserRepository
){

    fun getUserInfo(email: String): UserResponse {
        val user = userRepository.findUserByEmail(email)
            .orElseThrow{AlertException("유저를 찾지 못하였습니다")}
        return UserResponse.of(user);
    }
    @Transactional
    fun registerUser(registerRequest: RegisterRequest): UserResponse {
        val email :String= registerRequest.email
        if (userRepository.existsUserByEmail(email)) {
            throw AlertException("이메일이 중복되었습니다.")
        }
        val user = User(
            email,
            passwordEncoder.encode(registerRequest.password),
            registerRequest.username,
            OauthPlatform.NONE
            )
        userRepository.save(user)
        return UserResponse.of(user)
    }

    @Transactional
    fun updateUserInfo(email: String, modificationRequest: ModificationRequest): UserResponse {
        val user = userRepository.findUserByEmail(email)
            .orElseThrow { AlertException("유저를 찾지 못하였습니다") }
        user.modify(modificationRequest, passwordEncoder)
        return UserResponse.of(user)
    }

    @Transactional
    fun updateProfileImage(email: String, modificationImageRequest: ModificationImageRequest): UserResponse {
        val user = userRepository.findUserByEmail(email)
            .orElseThrow { AlertException("유저를 찾지 못하였습니다") }
        user.updateProfileImage(modificationImageRequest)
        return UserResponse.of(user)
    }

    fun checkEmailExists(email: String): EmailCheckResponse {
        return EmailCheckResponse(userRepository.existsUserByEmail(email))
    }
}