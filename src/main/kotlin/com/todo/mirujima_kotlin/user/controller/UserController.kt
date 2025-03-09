package com.todo.mirujima_kotlin.user.controller

import com.todo.mirujima_kotlin.auth.dto.response.EmailCheckResponse
import com.todo.mirujima_kotlin.auth.util.AuthUtil
import com.todo.mirujima_kotlin.common.dto.CommonResponse
import com.todo.mirujima_kotlin.user.dto.request.ModificationImageRequest
import com.todo.mirujima_kotlin.user.dto.request.ModificationRequest
import com.todo.mirujima_kotlin.user.dto.request.RegisterRequest
import com.todo.mirujima_kotlin.user.dto.response.UserResponse
import com.todo.mirujima_kotlin.user.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/mirujima/user")
@Tag(name = "User", description = "유저 정보 조회 API")
class UserController (
    private val userService: UserService
){
    @PostMapping
    @Operation(
        summary = "회원가입 API",
        description = "유저네임, 이메일, 비밀번호를 받아 회원가입을 진행합니다.",
        responses = [ApiResponse(responseCode = "200", description = "회원가입 성공"), ApiResponse(
            responseCode = "400",
            description = "회원가입 실패"
        )]
    )
    fun register(@RequestBody registerRequest: RegisterRequest): CommonResponse<UserResponse> {
        val response = userService.registerUser(registerRequest)
        return CommonResponse<UserResponse>().success(response)
    }

    @PutMapping
    @Operation(
        summary = "유저 정보 수정 API",
        description = "유저네임, 이메일, 비밀번호를 받아 수정을 진행합니다.",
        responses = [ApiResponse(responseCode = "200", description = "유저 정보 수정 성공"), ApiResponse(
            responseCode = "400",
            description = "유저 정보 수정 실패"
        )]
    )
    fun modify(@RequestBody modificationRequest: ModificationRequest): CommonResponse<UserResponse> {
        val response = userService.updateUserInfo(AuthUtil.email, modificationRequest)
        return CommonResponse<UserResponse>().success(response)
    }

    @PutMapping("/image")
    @Operation(
        summary = "유저 이미지 프로필 수정 API",
        description = "유저 프로필 이미지 정보를 받아 수정을 진행합니다.",
        responses = [ApiResponse(
            responseCode = "200",
            description = "유저 프로필 이미지 수정 성공"
        ), ApiResponse(responseCode = "400", description = "유저 프로필 이미지 수정 실패")]
    )
    fun updateProfileImage(@RequestBody modificationImageRequest: ModificationImageRequest): CommonResponse<UserResponse> {
        val response = userService.updateProfileImage(AuthUtil.email, modificationImageRequest)
        return CommonResponse<UserResponse>().success(response)
    }

    @GetMapping
    @Operation(
        summary = "유저 정보 조회 API",
        description = "토큰을 활용해 유저 정보를 조회한다.",
        responses = [ApiResponse(responseCode = "200", description = "유저 정보 조회 성공."), ApiResponse(
            responseCode = "400",
            description = "유저 정보 조회 실패"
        )]
    )
    fun getUserInfo(): UserResponse {
        return userService.getUserInfo(AuthUtil.email)
    }

    @GetMapping("/exists")
    @Operation(
        summary = "이메일 중복 검사 API",
        description = "이메일을 받아서 중복인지 검사한다.",
        responses = [ApiResponse(responseCode = "200", description = "이메일 중복 검사 성공."), ApiResponse(
            responseCode = "400",
            description = "이메일 검사 실패. 잘못된 이메일 형식이거나 서버 오류로 인해 검사가 실패했습니다."
        )]
    )
    fun checkEmailExists(@RequestParam email: String): CommonResponse<EmailCheckResponse> {
        return CommonResponse<EmailCheckResponse>().success(userService.checkEmailExists(email))
    }
}