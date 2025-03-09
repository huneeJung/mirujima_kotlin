package com.todo.mirujima_kotlin.auth.service.oauth

import com.fasterxml.jackson.databind.JsonNode
import com.todo.mirujima_kotlin.auth.dto.OAuthUserInfo
import com.todo.mirujima_kotlin.common.exception.AlertException
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate

@Service
class GoogleOAuthService(
    @Value("\${google.tokenUrl}")
    private val tokenUrl: String,
    @Value("\${google.userInfoUrl}")
    private val userInfoUrl: String,
    @Value("\${google.client.id}")
    private val clientId: String,
    @Value("\${google.client.secret}")
    private val clientSecret: String,
    @Value("\${google.redirect.uri}")
    private val redirectUri: String
) : OAuthService {

    private val restTemplate: RestTemplate = RestTemplate()

    override fun getAccessToken(code: String): String {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED

        val params = LinkedMultiValueMap<String, String>()
        params.add("code", code)
        params.add("client_id", clientId)
        params.add("client_secret", clientSecret)
        params.add("redirect_uri", redirectUri)
        params.add("grant_type", "authorization_code")

        val request = HttpEntity(params, headers)
        val response: ResponseEntity<JsonNode> =
            restTemplate.postForEntity(tokenUrl, request, JsonNode::class.java)
        val responseBody: JsonNode? = response.body

        if (response.statusCode !== HttpStatus.OK || responseBody == null) {
            throw AlertException("Google OAuth 인증 에러가 발생하였습니다.")
        }
        return responseBody["access_token"].asText()
    }

    override fun getUserInfo(accessToken: String): OAuthUserInfo {
        val headers = HttpHeaders()
        headers.setBearerAuth(accessToken)
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED

        val request = HttpEntity<Any>(headers)
        val response = restTemplate.postForEntity(userInfoUrl, request, OAuthUserInfo::class.java)
        val responseBody = response.body

        if (response.statusCode !== HttpStatus.OK || responseBody == null) {
            throw AlertException("Google 회원 정보 조회중 에러가 발생하였습니다.")
        }
        return responseBody
    }
}

