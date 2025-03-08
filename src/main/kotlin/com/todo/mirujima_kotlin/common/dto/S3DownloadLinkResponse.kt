package com.todo.mirujima_kotlin.common.dto

import io.swagger.v3.oas.annotations.media.Schema

class S3DownloadLinkResponse {
    @Schema(description = "미리 서명된 S3 링크", example = "https://torip.s3.ap-northeast-2.amazonaws.com/c79dffc9...")
    var signedUrl : String ? = null
}