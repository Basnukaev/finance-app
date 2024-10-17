package com.basnukaev.finance.app.service

import com.basnukaev.finance.app.config.AppLocale
import org.springframework.context.MessageSource
import org.springframework.stereotype.Service
import java.util.*

@Service
class LocalizationService(
    private val messageSource: MessageSource,
) {

    fun getMsg(
        messageCode: String,
        vararg params: String?,
        locale: Locale = AppLocale.RU,
    ): String {
        return messageSource.getMessage(messageCode, params, locale)
    }
}