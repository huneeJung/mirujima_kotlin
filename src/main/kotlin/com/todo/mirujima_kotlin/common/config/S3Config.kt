package com.todo.mirujima_kotlin.common.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.presigner.S3Presigner

@Configuration
class S3Config(
    @Value("\${cloud.aws.credentials.access-key}")
    val accessKey : String,
    @Value("\${cloud.aws.credentials.secret-key}")
    val secretKey : String,
    @Value("\${cloud.aws.region.static}")
    val region : String
) {

    @Bean
    fun s3Presigner() : S3Presigner
    = S3Presigner.builder()
        .region(Region.of(region))
        .credentialsProvider(
            StaticCredentialsProvider.create(
            AwsBasicCredentials.create(accessKey, secretKey))
        ).build();

}