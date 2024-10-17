package com.basnukaev.finance.app.telegram.provider

import com.basnukaev.finance.app.entity.User
import com.basnukaev.finance.app.service.UserService
import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.Injectable
import eu.vendeli.tgbot.interfaces.marker.Autowiring
import eu.vendeli.tgbot.types.internal.ProcessedUpdate
import eu.vendeli.tgbot.types.internal.userOrNull
import org.springframework.stereotype.Component

@Component
@Injectable
class UserProvider(
    private val userService: UserService,
) : Autowiring<User> {

    override suspend fun get(update: ProcessedUpdate, bot: TelegramBot): User {
        val telegramUser = update.userOrNull ?: throw NullPointerException("Telegram user not found!")
        return userService.findByTelegramUserId(telegramUser.id)
            ?: throw IllegalStateException("User demanded in wrong place!")
    }
}
