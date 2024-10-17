package com.basnukaev.finance.app.telegram.aop;

import com.basnukaev.finance.app.telegram.provider.CallbackQueryId
import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.api.answer.answerCallbackQuery
import kotlinx.coroutines.reactor.mono
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import eu.vendeli.tgbot.types.User as TelegramUser

@Aspect
@Component
class CallbackQueryAspect {

    @Order(10)
    @Around(
        """
        @annotation(eu.vendeli.tgbot.annotations.CommandHandler.CallbackQuery)
        """
    )
    fun answerCallbackQuery(joinPoint: ProceedingJoinPoint) = mono {
        val args = joinPoint.args
        val telegramUser = args.getByTyp<TelegramUser>()
        val bot = args.getByTyp<TelegramBot>()
        val callbackQueryId = args.getByTyp<CallbackQueryId>()
        answerCallbackQuery(callbackQueryId.get()).send(telegramUser, bot)
    }.then(joinPoint.proceed() as Mono<*>)
}
