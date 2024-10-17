package com.basnukaev.finance.app.repository

import com.basnukaev.finance.app.entity.Expense
import org.springframework.data.jpa.repository.JpaRepository
import java.time.Instant

interface ExpenseRepository : JpaRepository<Expense, Long> {

    fun findByTimestampBetween(startTimestamp: Instant, endTimestamp: Instant): List<Expense>
}