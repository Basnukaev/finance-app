package com.basnukaev.finance.app.service

import com.basnukaev.finance.app.entity.User
import com.basnukaev.finance.app.repository.CategoryRepository
import com.basnukaev.finance.app.repository.ExpenseRepository
import com.basnukaev.finance.app.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val categoryRepository: CategoryRepository,
    private val expenseRepository: ExpenseRepository
) {

    fun findByTelegramUserId(id: Long): User? = userRepository.findByTelegramUserId(id)

    fun createUser(user: User): User {
        if (user.id != null) {
            throw IllegalArgumentException("User id must be null")
        }
        categoryRepository.getBaseCategories()
            .forEach { user.categories.add(it) }
        return userRepository.save(user)
    }
}
