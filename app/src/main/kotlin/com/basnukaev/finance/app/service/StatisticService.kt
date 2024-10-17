package com.basnukaev.finance.app.service

import com.basnukaev.finance.app.moscowZoneId
import org.springframework.stereotype.Service
import java.time.DayOfWeek.MONDAY
import java.time.Instant
import java.time.LocalDate

@Service
class StatisticService {

    fun getStartAndEndByStatisticPeriod(statisticPeriod: StatisticPeriod): Pair<Instant, Instant> {
        val first = 1
        val startTimestamp: Instant = when (statisticPeriod) {
            StatisticPeriod.DAY -> LocalDate.now().atStartOfDay(moscowZoneId).toInstant()
            StatisticPeriod.WEEK -> LocalDate.now().with(MONDAY).atStartOfDay(moscowZoneId).toInstant()
            StatisticPeriod.MONTH -> LocalDate.now().withDayOfMonth(first).atStartOfDay(moscowZoneId).toInstant()
            StatisticPeriod.YEAR -> LocalDate.now().withDayOfYear(first).atStartOfDay(moscowZoneId).toInstant()
            StatisticPeriod.SELECT_PERIOD -> throw IllegalArgumentException("SELECT_PERIOD is not supported")
        }

        return Pair(startTimestamp, Instant.now())
    }
}