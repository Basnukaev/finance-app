package com.basnukaev.finance.app.telegram.aop

import com.basnukaev.finance.app.entity.User
import com.basnukaev.finance.app.redis.state.UserIdKey
import com.basnukaev.finance.app.service.LocalizationService
import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.api.message.sendMessage
import kotlinx.coroutines.reactor.mono
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.core.annotation.Order
import org.springframework.data.keyvalue.repository.KeyValueRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Aspect
@Component
class CheckStateAspect(
    private val keyValueRepositories: List<KeyValueRepository<out UserIdKey, Long>>,
    private val localizationService: LocalizationService,
) {

    @Order(20)
    @Around(
        """
        @annotation(com.basnukaev.finance.app.telegram.annotation.CheckState)
        """
    )
    fun checkState(joinPoint: ProceedingJoinPoint) = mono {
        val args = joinPoint.args
        val user = args.getByTyp<User>()
        val anyStateExists = keyValueRepositories.any {
            it.findByIdOrNull(user.id!!) != null
        }

        val telegramUser = args.getByTyp<eu.vendeli.tgbot.types.User>()
        val bot = args.getByTyp<TelegramBot>()
        if (!anyStateExists) {
            sendMessage(localizationService.getMsg("error.state-not-found"))
                .send(telegramUser, bot)
            return@mono false
        } else {
            return@mono true
        }
    }.flatMap { anyStateExists ->
        if (anyStateExists) {
            joinPoint.proceed() as Mono<*>
        } else {
            Mono.empty()
        }
    }
}