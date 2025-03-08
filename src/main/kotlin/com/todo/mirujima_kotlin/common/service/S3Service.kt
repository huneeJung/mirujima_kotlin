package com.todo.mirujima_kotlin.common.service

import com.todo.mirujima_kotlin.common.dto.S3DownloadLinkResponse
import com.todo.mirujima_kotlin.common.dto.S3UploadLinkResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest
import java.time.Duration
import java.time.LocalDate
import java.util.*

@Service
class S3Service(
    private val s3Presigner : S3Presigner,
    @Value("\${cloud.aws.s3.bucket}")
    private val bucket: String? = null
) {

    fun generateS3UploadUrl(fileName: String): S3UploadLinkResponse {
        try {
            // 현재 날짜 기준 디렉토리 경로 생성
            val now = LocalDate.now()
            val datePath = String.format(
                "%d/%02d/%02d/",
                now.year,
                now.monthValue,
                now.dayOfMonth
            )
            // 파일 확장자 추출
            val fileExtension = Optional.ofNullable(fileName)
                .filter { file: String -> file.contains(".") }
                .map { file: String ->
                    file.substring(
                        fileName.lastIndexOf(".")
                    )
                }
                .orElseThrow()
            // 고유한 파일명 생성 (날짜 디렉토리 포함)
            val uniqueFileName = datePath + UUID.randomUUID() + fileExtension
            val putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(uniqueFileName)
                .build()
            // 서명된 S3 PUT URL 요청
            val presignRequest = s3Presigner.presignPutObject { req: PutObjectPresignRequest.Builder ->
                req.putObjectRequest(putObjectRequest)
                    .signatureDuration(Duration.ofMinutes(1))
            }
            return S3UploadLinkResponse()
                .apply {
                    signedUrl = presignRequest.url().toExternalForm()
                    filePath = uniqueFileName
                }
        } catch (e: Exception) {
            throw RuntimeException("Signed PUT URL 생성에 실패하였습니다.", e)
        }
    }

    fun generateS3DownloadUrl(filePath: String?): S3DownloadLinkResponse {
        try {
            val getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(filePath)
                .build()
            // 서명된 S3 GET URL 요청
            val presignedGetObjectRequest = s3Presigner.presignGetObject { req: GetObjectPresignRequest.Builder ->
                req.getObjectRequest(getObjectRequest)
                    .signatureDuration(Duration.ofMinutes(10))
            }
            return S3DownloadLinkResponse().apply {
                   signedUrl = presignedGetObjectRequest.url().toExternalForm()
            }
        } catch (e: java.lang.Exception) {
            throw java.lang.RuntimeException("Signed GET URL 생성에 실패하였습니다.", e)
        }
    }

}