package com.basnukaev.finance.app.config

import java.util.*

object AppLocale {
    val RU: Locale = Locale.of("ru", "RU")
    val EN: Locale = Locale.of("en", "EN")

    fun getByLanguageCode(languageCode: String?): Locale {
        return when (languageCode) {
            "ru" -> RU
            "en" -> EN
            else -> RU
        }
    }
}