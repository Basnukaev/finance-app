package com.basnukaev.finance.app.telegram.config

import eu.vendeli.tgbot.interfaces.ctx.ClassManager
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import kotlin.reflect.KClass

@Component
class ApplicationContextManager(
    private val applicationContext: ApplicationContext,
) : ClassManager {

    override fun getInstance(kClass: KClass<*>, vararg initParams: Any?): Any =
        applicationContext.getBean(kClass.java, initParams)
}
