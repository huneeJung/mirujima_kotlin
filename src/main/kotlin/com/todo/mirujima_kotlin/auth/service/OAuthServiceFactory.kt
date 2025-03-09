package com.todo.mirujima_kotlin.auth.service

import com.todo.mirujima_kotlin.auth.dto.OAuthUserInfo
import com.todo.mirujima_kotlin.auth.service.oauth.GoogleOAuthService
import com.todo.mirujima_kotlin.auth.service.oauth.KakaoOAuthService
import com.todo.mirujima_kotlin.auth.service.oauth.OAuthService
import com.todo.mirujima_kotlin.common.exception.AlertException
import com.todo.mirujima_kotlin.user.entity.OauthPlatform
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Service
import java.util.*

@Service
class OAuthServiceFactory(
        private val googleOAuthService: GoogleOAuthService,
        private val kakaoOAuthService: KakaoOAuthService
) {

    private val oAuthServiceMap: MutableMap<String, OAuthService> = HashMap<String, OAuthService>()

    @PostConstruct
    fun init() {
        oAuthServiceMap[OauthPlatform.GOOGLE.name] = googleOAuthService
        oAuthServiceMap[OauthPlatform.KAKAO.name] = kakaoOAuthService
    }

    fun getUserInfo(platform: String, code: String): OAuthUserInfo {
        val service = Optional.ofNullable(oAuthServiceMap[platform])
            .orElseThrow{AlertException("잘못된 인증 플랫폼입니다.")}
        val accessToken = service.getAccessToken(code)
        return service.getUserInfo(accessToken)
    }
}
