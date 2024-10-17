package com.basnukaev.finance.app.dto

data class ExpenseStatisticsDto(
    val totalAmount: Double,
    val mandatoryAmount: Double,
    val nonMandatoryAmount: Double,
    val categoryStats: Map<String, Double>
)
