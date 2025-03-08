package com.todo.mirujima_kotlin.user.repository

import com.todo.mirujima_kotlin.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface UserRepository : JpaRepository<User, Long> {

    fun findUserByEmail(email: String): Optional<User>

    fun findUserById(userId: Long): Optional<User>

    fun existsUserByEmail(email: String): Boolean

    fun existsUserByUsername(username: String): Boolean
}

