package com.basnukaev.finance.app.repository

import com.basnukaev.finance.app.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {

    fun findByTelegramUserId(telegramUserId: Long): User?

}