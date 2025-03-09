package com.todo.mirujima_kotlin.auth.controller

import com.todo.mirujima_kotlin.auth.dto.request.LoginRequest
import com.todo.mirujima_kotlin.auth.dto.request.TokenRequest
import com.todo.mirujima_kotlin.auth.dto.response.LoginResponse
import com.todo.mirujima_kotlin.auth.dto.response.TokenResponse
import com.todo.mirujima_kotlin.auth.service.AuthService
import com.todo.mirujima_kotlin.common.dto.CommonResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/mirujima/auth")
@Tag(name = "Auth", description = "인증 관련 API")
class AuthController (
    private val authService: AuthService
){

    @PostMapping("/login")
    @Operation(
        summary = "로그인 API",
        description = "이메일과 비밀번호를 받아 토큰을 생성합니다.",
        responses = [ApiResponse(responseCode = "200", description = "로그인 성공"), ApiResponse(
            responseCode = "400",
            description = "로그인 실패"
        )]
    )
    fun login(@RequestBody loginRequest: LoginRequest): CommonResponse<LoginResponse> {
        val response = authService.login(loginRequest)
        return CommonResponse<LoginResponse>().success(response)
    }

    @PostMapping("/refresh")
    @Operation(
        summary = "토큰 갱신 API",
        description = "리프레시 토큰을 받아 새로운 액세스 토큰을 생성합니다.",
        responses = [ApiResponse(responseCode = "200", description = "토큰 갱신 성공"), ApiResponse(
            responseCode = "400",
            description = "토큰 갱신 실패"
        )]
    )
    fun refresh(@RequestBody refreshToken: TokenRequest): CommonResponse<TokenResponse> {
        val response = authService.refresh(refreshToken.refreshToken)
        return CommonResponse<TokenResponse>().success(response)
    }

    @GetMapping("/{platform}")
    @Operation(
        summary = "OAuth API",
        description = "토큰을 받아 사용자 회원가입 혹은 로그인을 시도합니다.",
        responses = [ApiResponse(responseCode = "200", description = "OAuth 인증 성공"), ApiResponse(
            responseCode = "400",
            description = "OAuth 인증 실패"
        )]
    )
    fun loginWithGoogle(
        @PathVariable(name = "platform") platform: String, @RequestParam code: String
    ): CommonResponse<LoginResponse> {
        val response = authService.authenticateWithOAuth(platform, code)
        return CommonResponse<LoginResponse>().success(response)
    }
}
