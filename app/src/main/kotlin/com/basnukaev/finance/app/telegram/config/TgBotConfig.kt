package com.basnukaev.finance.app.telegram.config

import eu.vendeli.tgbot.TelegramBot
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TgBotConfig(
    @Value("\${TELEGRAM_BOT_TOKEN}")
    private val botToken: String,
) {

    @Bean
    fun telegramBot(
        applicationContextManager: ApplicationContextManager,
        inputListenerRedisImpl: InputListenerRedisImpl,
    ): TelegramBot = TelegramBot(
        token = botToken,
        commandsPackage = "com.basnukaev.finapp.telegram",
        botConfiguration = {
            classManager = applicationContextManager
            inputListener = inputListenerRedisImpl
        }
    )

}