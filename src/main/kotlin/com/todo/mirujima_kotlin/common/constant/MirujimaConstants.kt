package com.todo.mirujima_kotlin.common.constant

object MirujimaConstants {

    object Goal {
        const val GOAL = "목표"
        const val PAGE_SIZE = 5
    }

    object Note {
        const val NOTE = "노트"
        const val PAGE_SIZE = 5
    }

    object Todo {
        const val TODO = "할일"
        const val PAGE_SIZE = 30
        const val TASK_LIMIT_PER_TRIP_STATUS = 60
        const val TASK_LIMIT = 180
    }

    object HttpConstant {
        const val SUCCESS_CODE = 200
        const val SERVER_FAIL_CODE = 500
        const val CLIENT_FAIL_CODE = 400
        const val UNAUTHORIZED = 401
        const val SUCCESS_MESSAGE = "성공"
    }
}
