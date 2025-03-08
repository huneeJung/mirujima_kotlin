package com.todo.mirujima_kotlin.auth.util

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.time.ZoneId
import java.util.*
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

@Component
class JwtUtil(
    @Value("\${jwt.secretKey}") secretKeyString: String,
    @Value("\${jwt.expiredAccessTokenMs}") val expiredAccessTokenMs: Long,
    @Value("\${jwt.expiredRefreshTokenMs}") val expiredRefreshTokenMs: Long
) {
    val secretKey: SecretKey = SecretKeySpec(
        secretKeyString.toByteArray(StandardCharsets.UTF_8),
        Jwts.SIG.HS256.key().build().algorithm
    )

    fun getLoginId(token: String) = getClaim(token,"loginId")

    fun isExpired(token:String) :Boolean{
        return try{
            Jwts.parser()
                .verifyWith(secretKey).build()
                .parseSignedClaims(token)
                .payload.expiration.before(Date());
        }catch (e:ExpiredJwtException){
            true;
        }
    }

    fun createAccessToken(loginId:String)
    = Jwts.builder()
        .claim("loginId", loginId)
        .claim("type","access")
        .issuedAt(Date(System.currentTimeMillis()))
        .signWith(secretKey)
        .compact()

    fun getAccessTokenExpiredAt()
    = Date(System.currentTimeMillis() + expiredAccessTokenMs)

    fun createRefreshToken(loginId:String)
    = Jwts.builder()
        .claim("loginId", loginId)
        .claim("type", "refresh")
        .issuedAt(Date(System.currentTimeMillis()))
        .expiration(Date(System.currentTimeMillis() + expiredRefreshTokenMs))
        .signWith(secretKey)
        .compact();

    fun refreshAccessToken(refreshToken:String) :String{
        if (isExpired(refreshToken)) throw ExpiredJwtException(null, null, "만료된 토큰 값입니다.")
        val tokenType = getClaim(refreshToken, "type")
        if(tokenType == "refresh") throw IllegalArgumentException("토큰값이 다릅니다.")
        val loginId = getLoginId(refreshToken)
        return createAccessToken(loginId);
    }

    private fun getClaim(token:String,name:String)
    = Jwts.parser().verifyWith(secretKey).build()
        .parseSignedClaims(token)
        .payload[name] as String

    fun validateToken(token:String,userDetails:UserDetails)
    = getLoginId(token) == userDetails.username && !isExpired(token);

    fun getExpiredAt()
    = getAccessTokenExpiredAt().toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime();

}