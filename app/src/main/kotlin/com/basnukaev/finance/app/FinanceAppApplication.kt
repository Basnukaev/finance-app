package com.basnukaev.finance.app

import eu.vendeli.tgbot.TelegramBot
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import java.time.ZoneId

val moscowZoneId: ZoneId = ZoneId.of("Europe/Moscow")

@EnableJpaAuditing
@EnableAspectJAutoProxy
@SpringBootApplication(scanBasePackages = ["com.basnukaev.finance.app"])
class FinanceAppApplication

suspend fun main(args: Array<String>) {
    val applicationContext = runApplication<FinanceAppApplication>(*args)
    val telegramBot = applicationContext.getBean(TelegramBot::class.java)

    telegramBot.handleUpdates()
}
