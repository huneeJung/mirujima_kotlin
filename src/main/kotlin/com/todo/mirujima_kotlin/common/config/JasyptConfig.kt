package com.todo.mirujima_kotlin.common.config

import org.jasypt.encryption.StringEncryptor
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JasyptConfig(
    @Value("\${jasypt.encryptor.key}")
    val key : String
) {

    @Bean(name = ["jasyptStringEncryptor"])
    fun stringEncryptor() : StringEncryptor{
        val config = SimpleStringPBEConfig().apply {
            password = key // 암호화에 사용되는 비밀 키
            algorithm = "PBEWithMD5AndDES" // 사용할 암호화 알고리즘
            providerName = "SunJCE" // 암호화 제공자
            stringOutputType = "base64" // 암호화된 문자열의 출력 형식
            setPoolSize("1") // 사용할 암호화 엔진의 수
            setKeyObtentionIterations("1000") // 키 생성을 위해 수행되는 반복 횟수
            setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator") // 솔트 생성기
        }
        return PooledPBEStringEncryptor().apply { setConfig(config) }
    }

}