package com.basnukaev.finance.app.service

import com.basnukaev.finance.app.dto.ExpenseStatisticsDto
import com.basnukaev.finance.app.entity.Expense
import com.basnukaev.finance.app.entity.User
import com.basnukaev.finance.app.repository.CategoryRepository
import com.basnukaev.finance.app.repository.ExpenseRepository
import com.basnukaev.finance.app.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

@Service
class ExpenseService(
    private val userRepository: UserRepository,
    private val expenseRepository: ExpenseRepository,
    private val categoryRepository: CategoryRepository,
) {

    @Transactional
    fun addExpenseToUser(
        user: User,
        description: String,
        amountStr: String,
        comment: String?,
        categoryName: String,
        isMandatory: Boolean
    ): Expense {
        val category = categoryRepository.findByName(categoryName)
            ?: throw IllegalArgumentException("Category not found")

        val amount = parseAmount(amountStr)!!
        val formattedDescription = description.capitalizeFirstChar()
        val formattedComment = comment?.capitalizeFirstChar()

        val expense = Expense(
            description = formattedDescription,
            amount = amount,
            comment = formattedComment,
            isMandatory = isMandatory,
            category = category,
            user = user
        )

        return expenseRepository.save(expense)
    }

    fun parseAmount(amountStr: String): Double? {
        return if (amountStr.endsWithIgnoreCase("k", "к")) {
            amountStr.dropLast(1).toDoubleOrNull()?.times(1000)
        } else {
            amountStr.toDoubleOrNull()
        }
    }

    fun getStatisticsForPeriodByUserId(userId: Long, startTimestamp: Instant, endTimestamp: Instant): ExpenseStatisticsDto {
        val expenses = expenseRepository.findByTimestampBetween(startTimestamp, endTimestamp)

        val totalAmount = expenses.sumOf { it.amount }
        val mandatoryAmount = expenses.filter { it.isMandatory }.sumOf { it.amount }
        val nonMandatoryAmount = totalAmount - mandatoryAmount

        val categoryStats = expenses.groupBy { it.category.name }
            .mapValues { categoryToExpense ->
                categoryToExpense.value.sumOf { expense -> expense.amount }
            }

        return ExpenseStatisticsDto(
            totalAmount = totalAmount,
            mandatoryAmount = mandatoryAmount,
            nonMandatoryAmount = nonMandatoryAmount,
            categoryStats = categoryStats
        )
    }

    @Suppress("SameParameterValue")
    private fun String.endsWithIgnoreCase(vararg suffix: String): Boolean = suffix.any {
        this.endsWith(it, ignoreCase = true)
    }

    private fun String.capitalizeFirstChar(): String {
        return if (isNotEmpty()) {
            replaceFirstChar {
                if (it.isLowerCase()) {
                    it.titlecase(Locale.getDefault())
                } else {
                    it.toString()
                }
            }
        } else {
            this
        }
    }
}
