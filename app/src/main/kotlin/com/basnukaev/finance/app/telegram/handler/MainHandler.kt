package com.basnukaev.finance.app.telegram.handler

import com.basnukaev.finance.app.entity.User
import com.basnukaev.finance.app.service.LocalizationService
import com.basnukaev.finance.app.service.UserService
import com.basnukaev.finance.app.telegram.handler.CallbackQueryValue.STATISTIC
import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.CommandHandler
import eu.vendeli.tgbot.api.message.sendMessage
import eu.vendeli.tgbot.types.ParseMode
import org.springframework.stereotype.Component
import eu.vendeli.tgbot.types.User as TelegramUser

@Component
class MainHandler(
    private val userService: UserService,
    private val localizationService: LocalizationService,
) {

    @CommandHandler(["/start"])
    suspend fun start(
        telegramUser: TelegramUser,
        bot: TelegramBot,
    ) {
        var isNewUser = false
        val user = userService.findByTelegramUserId(telegramUser.id)
            ?: userService.createUser(
                User(
                    telegramUserId = telegramUser.id,
                    telegramUsername = telegramUser.username,
                    languageCode = telegramUser.languageCode,
                    firstName = telegramUser.firstName,
                    lastName = telegramUser.lastName
                )
            ).also {
                isNewUser = true
            }

        val messageCode = if (isNewUser) "start.welcome" else "start.welcome-back"
        sendMessage(localizationService.getMsg(messageCode, user.firstName))
            .options { parseMode = ParseMode.MarkdownV2 }
            .send(telegramUser, bot)
    }

    @CommandHandler(["/menu"])
    suspend fun menu(
        telegramUser: TelegramUser,
        bot: TelegramBot,
    ) {
        sendMessage(localizationService.getMsg("menu.chose-option"))
            .inlineKeyboardMarkup {
                localizationService.getMsg("menu.statistic").callback(STATISTIC)
            }
            .send(telegramUser, bot)
    }
}
