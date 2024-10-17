package com.basnukaev.finance.app.telegram.provider

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.Injectable
import eu.vendeli.tgbot.interfaces.marker.Autowiring
import eu.vendeli.tgbot.types.internal.ProcessedUpdate
import org.springframework.stereotype.Component

@Component
@Injectable
class CallbackQueryIdProvider : Autowiring<CallbackQueryId> {

    override suspend fun get(update: ProcessedUpdate, bot: TelegramBot): CallbackQueryId =
        CallbackQueryId(update.origin.callbackQuery?.id)
}

class CallbackQueryId(value: String?) : Container<String>(value)
