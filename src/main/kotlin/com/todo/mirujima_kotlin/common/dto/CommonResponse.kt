package com.todo.mirujima_kotlin.common.dto

import io.swagger.v3.oas.annotations.media.Schema
import com.todo.mirujima_kotlin.common.constant.MirujimaConstants.HttpConstant.SUCCESS_CODE
import com.todo.mirujima_kotlin.common.constant.MirujimaConstants.HttpConstant.SUCCESS_MESSAGE

@Schema(description = "공통 응답 객체")
data class CommonResponse<T>(
    @Schema(description = "성공 여부")
    var success: Boolean? = null,
    @Schema(description = "결과 코드")
    var code: Int? = null,
    @Schema(description = "결과 메세지")
    var message: String? = null,
    @Schema(description = "결과 데이터")
    var result: T? = null
) {

    fun success(result: T): CommonResponse<T> {
        return CommonResponse(
            success = true,
            code = SUCCESS_CODE,
            message = SUCCESS_MESSAGE,
            result = result
        )
    }

    fun fail(code: Int, message: String): CommonResponse<T> {
        return CommonResponse(
            success = false,
            code = code,
            message = message,
            result = null
        )
    }
}
