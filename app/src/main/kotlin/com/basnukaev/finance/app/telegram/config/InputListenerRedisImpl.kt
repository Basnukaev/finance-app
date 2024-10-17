package com.basnukaev.finance.app.telegram.config

import eu.vendeli.tgbot.interfaces.ctx.InputListener
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component

@Component
class InputListenerRedisImpl(
    private val stringRedisTemplate: StringRedisTemplate,
) : InputListener {
    private val keyPrefix = "telegram:input-listener:"

    override fun del(telegramId: Long) {
        stringRedisTemplate.delete(telegramId.toKey())
    }

    override suspend fun delAsync(telegramId: Long): Deferred<Boolean> = withContext(Dispatchers.IO) {
        async {
            stringRedisTemplate.delete(telegramId.toKey())
        }
    }

    override fun get(telegramId: Long): String? = stringRedisTemplate.opsForValue().get(telegramId.toKey())

    override suspend fun getAsync(telegramId: Long): Deferred<String?> = withContext(Dispatchers.IO) {
        async {
            stringRedisTemplate.opsForValue().get(telegramId.toKey())
        }
    }

    override fun set(telegramId: Long, identifier: String) {
        stringRedisTemplate.opsForValue().set(telegramId.toKey(), identifier)
    }

    override suspend fun setAsync(telegramId: Long, identifier: String): Deferred<Boolean> =
        withContext(Dispatchers.IO) {
            async {
                stringRedisTemplate.opsForValue().set(telegramId.toKey(), identifier)
                return@async true
            }
        }

    private fun Long.toKey() = "$keyPrefix$this"
}
