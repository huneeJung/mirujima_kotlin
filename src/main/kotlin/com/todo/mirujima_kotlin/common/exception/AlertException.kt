package com.todo.mirujima_kotlin.common.exception

class AlertException(
    message: String?,
    cause: Throwable? = null
) : RuntimeException(message)