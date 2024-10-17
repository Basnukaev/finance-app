package com.basnukaev.finance.app.redis.repository

import com.basnukaev.finance.app.redis.state.ExpenseState
import org.springframework.data.keyvalue.repository.KeyValueRepository

interface ExpenseStateKeyValueRepository : KeyValueRepository<ExpenseState, Long> {
}