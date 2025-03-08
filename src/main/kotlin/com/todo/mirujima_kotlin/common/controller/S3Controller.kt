package com.todo.mirujima_kotlin.common.controller

import com.todo.mirujima_kotlin.common.dto.CommonResponse
import com.todo.mirujima_kotlin.common.dto.S3DownloadLinkResponse
import com.todo.mirujima_kotlin.common.dto.S3UploadLinkResponse
import com.todo.mirujima_kotlin.common.exception.AlertException
import com.todo.mirujima_kotlin.common.service.S3Service
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.atomic.AtomicInteger

@RestController
@RequestMapping("/mirujima/files")
@Tag(name = "File", description = "파일 관련 API")
class S3Controller(
    private val s3Service: S3Service
) {

    val UPLOAD_REQUEST_CNT :AtomicInteger = AtomicInteger()
    val DOWNLOAD_REQUEST_CNT :AtomicInteger = AtomicInteger()

    @PostMapping("/upload")
    @Operation(
        summary = "파일 업로드 URL 발급 API",
        description = "파일 업로드 URL을 발급합니다. (유효시간 1분)",
        responses = [ApiResponse(responseCode = "200", description = "성공")]
    )
    fun generateS3UploadUrl(@RequestParam("fileName") fileName: String?): CommonResponse<S3UploadLinkResponse> {
        // TODO 나중에 지워도 되지만 혹시나 많은 양의 업로드 요청시 비용 문제가 될 수 있으므로 임시 방편으로 제한을 둠
        UPLOAD_REQUEST_CNT.set(UPLOAD_REQUEST_CNT.get() + 1)
        if (UPLOAD_REQUEST_CNT.get() >= 100) throw AlertException("일일 업로드 제한 100회를 초과하였습니다")

        val s3UrlResponse = s3Service.generateS3UploadUrl(fileName!!)
        return CommonResponse<S3UploadLinkResponse>().success(s3UrlResponse)
    }

    @PostMapping("/download")
    @Operation(
        summary = "파일 다운로드 URL 발급 API",
        description = "파일 다운로드 URL을 발급합니다. (유효시간 10분)",
        responses = [ApiResponse(responseCode = "200", description = "성공")]
    )
    fun generateS3DownloadUrl(@RequestParam("fileName") filePath: String?): CommonResponse<S3DownloadLinkResponse> {
        // TODO 나중에 지워도 되지만 혹시나 많은 양의 다운로드 요청시 비용 문제가 될 수 있으므로 임시 방편으로 제한을 둠
        DOWNLOAD_REQUEST_CNT.set(DOWNLOAD_REQUEST_CNT.get() + 1)
        if (DOWNLOAD_REQUEST_CNT.get() >= 1000) throw AlertException("일일 다운로드 제한 1000회를 초과하였습니다")

        val s3UrlResponse = s3Service.generateS3DownloadUrl(filePath!!)
        return CommonResponse<S3DownloadLinkResponse>().success(s3UrlResponse)
    }

}