package com.todo.mirujima_be.common.exception

import io.jsonwebtoken.ExpiredJwtException
import jakarta.validation.ConstraintViolationException
import org.slf4j.LoggerFactory
import org.springframework.dao.DataAccessException
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.net.BindException

import com.todo.mirujima_kotlin.common.constant.MirujimaConstants.HttpConstant.CLIENT_FAIL_CODE
import com.todo.mirujima_kotlin.common.constant.MirujimaConstants.HttpConstant.SERVER_FAIL_CODE
import com.todo.mirujima_kotlin.common.constant.MirujimaConstants.HttpConstant.UNAUTHORIZED
import com.todo.mirujima_kotlin.common.dto.CommonResponse
import com.todo.mirujima_kotlin.common.exception.AlertException

@RestControllerAdvice
class GlobalExceptionHandler {

    private val log = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(Exception::class)
    fun defaultExceptionHandler(e: Exception): CommonResponse<*> {
        log.error("Exception", e)
        return CommonResponse<Any>().fail(SERVER_FAIL_CODE, e.message ?: "Unknown error")
    }

    @ExceptionHandler(DataAccessException::class)
    fun dataAccessExceptionHandler(e: DataAccessException): CommonResponse<*> {
        log.error("DataAccessException", e)
        return CommonResponse<Any>().fail(SERVER_FAIL_CODE, e.message ?: "Data access error")
    }

    @ExceptionHandler(AlertException::class)
    fun alertExceptionHandler(e: AlertException): CommonResponse<*> {
        log.warn("AlertException", e)
        return CommonResponse<Any>().fail(CLIENT_FAIL_CODE, e.message ?: "Alert exception")
    }

    @ExceptionHandler(ExpiredJwtException::class)
    fun expiredJwtExceptionHandler(e: ExpiredJwtException): CommonResponse<*> {
        log.warn("ExpiredJwtException", e)
        return CommonResponse<Any>().fail(UNAUTHORIZED, e.message ?: "JWT expired")
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun methodArgumentNotValidExceptionHandler(e: MethodArgumentNotValidException): CommonResponse<*> {
        log.warn("MethodArgumentNotValidException", e)
        val errorMessage = e.bindingResult.fieldErrors
            .map { it.defaultMessage }
            .firstOrNull() ?: "유효성 검사 오류 발생"
        return CommonResponse<Any>().fail(CLIENT_FAIL_CODE, errorMessage)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun constraintViolationExceptionHandler(e: ConstraintViolationException): CommonResponse<*> {
        log.warn("ConstraintViolationException", e)
        return CommonResponse<Any>().fail(CLIENT_FAIL_CODE, e.message ?: "Constraint violation error")
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun httpMessageNotReadableExceptionHandler(e: HttpMessageNotReadableException): CommonResponse<*> {
        log.warn("HttpMessageNotReadableException", e)
        return CommonResponse<Any>().fail(CLIENT_FAIL_CODE, e.message ?: "Invalid message")
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun illegalArgumentExceptionHandler(e: IllegalArgumentException): CommonResponse<*> {
        log.warn("IllegalArgumentException", e)
        return CommonResponse<Any>().fail(CLIENT_FAIL_CODE, e.message ?: "Illegal argument")
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun httpRequestMethodNotSupportedExceptionHandler(e: HttpRequestMethodNotSupportedException): CommonResponse<*> {
        log.warn("HttpRequestMethodNotSupportedException", e)
        return CommonResponse<Any>().fail(CLIENT_FAIL_CODE, e.message ?: "Method not supported")
    }

    @ExceptionHandler(BindException::class)
    fun bindExceptionHandler(e: BindException): CommonResponse<*> {
        return CommonResponse<Any>().fail(CLIENT_FAIL_CODE, e.message ?: "Bind exception")
    }
}
